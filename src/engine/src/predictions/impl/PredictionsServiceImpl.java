package predictions.impl;

import components.queue.management.ThreadPoolDelegate;
import dto.*;
import predictions.api.PredictionsService;
import world.*;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.environment.EnvVariablesUpdater;
import world.environment.api.ActiveEnvironment;
import world.environment.api.EnvironmentVariablesManager;
import world.environment.impl.ActiveEnvironmentImpl;
import world.exceptions.EntityPropertyNotExistException;
import world.factory.DTOFactory;
import world.file.reader.EngineFileReader;
import world.grid.Grid;
import world.property.api.AbstractPropertyDefinition;
import world.property.api.PropertyDefinition;
import world.property.api.PropertyInstance;
import world.property.impl.PropertyInstanceImpl;
import world.simulation.PastSimulation;
import world.simulation.SimulationExecutionManager;
import world.simulation.SimulationExecutor;
import world.simulation.SimulationInfoBuilder;

import java.util.*;


public class PredictionsServiceImpl implements PredictionsService {
    private SimulationExecutionManager simulationManager;


    @Override
    public FileReaderDTO readFileAndLoad(String fileName) {
       World mainWorld = null;
       EngineFileReader fileReader = new EngineFileReader();
       try {
           mainWorld = fileReader.checkFileValidation(fileName);
       } catch (Exception e) {
           return new FileReaderDTO(Boolean.FALSE, e.getMessage(), null);
       }
       simulationManager = new SimulationExecutionManager(mainWorld.getThreadCount());
       simulationManager.addWorldSimulation(mainWorld);
       Grid grid = mainWorld.getGrid();
       return new FileReaderDTO(Boolean.TRUE, null, new GridDTO(grid.getRows(), grid.getCols()));
    }

    @Override
    public SimulationInfoDTO getSimulationInformation() {
        SimulationInfoBuilder simulationInfoBuilder = new SimulationInfoBuilder();
        return simulationInfoBuilder.createSimulationInfo(simulationManager.getMainWorld());
    }

    @Override
    public void randomizeEnvProperties() {
        World world = simulationManager.getMainWorld();
        EnvironmentVariablesManager envVariablesManager = world.getEnvironmentVariablesManager();
        ActiveEnvironment activeEnvironment = envVariablesManager.createActiveEnvironment();

        for (PropertyDefinition propertyDefinition : envVariablesManager.getEnvironmentVariables()) {
            activeEnvironment.addPropertyInstance(new PropertyInstanceImpl(propertyDefinition, propertyDefinition.generateValue()));
        }
        world.setActiveEnvironment(activeEnvironment);
    }

    @Override
    public PropertiesDTO getEnvPropertiesDTO() {
        DTOFactory dtoFactory = new DTOFactory();
        List<PropertyDTO> DTOs = new ArrayList<>();
        World world = simulationManager.getMainWorld();
        for (PropertyDefinition envProperty : world.getEnvironmentVariablesManager().getEnvironmentVariables()) {
            DTOs.add(dtoFactory.createPropertyDTO(envProperty));
        }
        return new PropertiesDTO(DTOs);
    }

    @Override
    public void setEntitiesPopulation(List<EntityInitializationDTO> DTOs) {
        simulationManager.getMainWorld().setEntitiesPopulation(DTOs);
    }

    @Override
    public EnvVariableSetValidationDTO setEnvironmentVariables(List<UserInputEnvironmentVariableDTO> DTOs) {
        World world = simulationManager.getMainWorld();
        ActiveEnvironment activeEnvironment = world.getActiveEnvironment();

        for (UserInputEnvironmentVariableDTO dto : DTOs) {
            PropertyInstance propertyInstance = activeEnvironment.getProperty(dto.getName()).get();
            AbstractPropertyDefinition.PropertyType type = propertyInstance.getPropertyDefinition().getType();

            try {
                EnvVariablesUpdater envVariablesUpdater = new EnvVariablesUpdater();
                envVariablesUpdater.updateVariable(propertyInstance, type, dto);
            } catch (NumberFormatException e) {
                return new EnvVariableSetValidationDTO(Boolean.FALSE, String.format("Failed to assign the value \"%s\" to the environment variable \"%s\" due to incompatible types", dto.getValue(), dto.getName()));
            }
        }
        return new EnvVariableSetValidationDTO(Boolean.TRUE, null);
    }

    @Override
    public List<EnvVariablesDTO> getEnvVariablesDTOList(Integer id) {
        ActiveEnvironment activeEnvironment = simulationManager.getSpecificWorld(id).getActiveEnvironment();
        List<EnvVariablesDTO> lst = new ArrayList<>();
        for (PropertyInstance property : activeEnvironment.getEnvironmentVariables()) {
            lst.add(new EnvVariablesDTO(property.getPropertyDefinition().getName(), property.getValue().toString()));
        }
        return lst;
    }

    @Override
    public Integer runSimulation(ThreadPoolDelegate threadPoolDelegate) {
        World newWorld = simulationManager.getMainWorld().deepCopy();
        SimulationExecutor simulationExecutor = new SimulationExecutor(threadPoolDelegate);
        threadPoolDelegate.increaseSimulationsInQueue();
        simulationExecutor.setWorld(newWorld);
        simulationManager.addWorldSimulation(newWorld);
        Integer id = newWorld.getSimulationID();
        simulationManager.getThreadExecutor().execute(simulationExecutor);
        return id;
    }

    @Override
    public PastSimulationDTO getSimulationsDTO(Integer id) {
        DTOFactory dtoFactory = new DTOFactory();

        PastSimulation pastSimulation = simulationManager.getSpecificWorld(id).getPastSimulation();
        Collection<EntityDefinition> entityDefinitionCollection = pastSimulation.getEntities();
        List<PastEntityDTO> pastEntityDTOList = new ArrayList<>();
        for (EntityDefinition entityDefinition : entityDefinitionCollection) {
            List<PropertyDTO> propertyDTOList = new ArrayList<>();
            for (PropertyDefinition propertyDefinition : entityDefinition.getPropertiesList()) {
                PropertyDTO propertyDTO = dtoFactory.createPropertyDTO(propertyDefinition);
                propertyDTOList.add(propertyDTO);
            }
            pastEntityDTOList.add(new PastEntityDTO(entityDefinition.getName(), entityDefinition.getPopulation(), entityDefinition.getEntityInstances().size(), propertyDTOList));
        }
        List<EnvVariablesDTO> envVariablesDTOs = new ArrayList<>();
        for (PropertyInstance envVariable : pastSimulation.getActiveEnvironment().getEnvironmentVariables()) {
            envVariablesDTOs.add(dtoFactory.createEnvVariableDTO(envVariable));
        }

        return new PastSimulationDTO(id, pastEntityDTOList, pastSimulation.getEntityToPopulation(), pastSimulation.getDynamicPopulation(), envVariablesDTOs, pastSimulation.isRunning(), pastSimulation.getTicks(), pastSimulation.getSeconds());
    }


    @Override
    public HistogramDTO getHistogram(Integer id, String entityName, String propertyName) {
        Collection<EntityDefinition> entityDefinitions = simulationManager.getSpecificWorld(id).getEntityDefinitions();
        Map<Object, Integer> valueToAmount = new HashMap<>();

        for (EntityDefinition entityDefinition : entityDefinitions) {
            if (entityDefinition.getName().equals(entityName)) {
                List<EntityInstance> entityInstances = entityDefinition.getEntityInstances();
                for (EntityInstance entityInstance : entityInstances) {
                    Object key = (entityInstance.getPropertyByName(propertyName).getValue());

                    if (key instanceof Integer || key instanceof Float) {
                        Number numKey = (Number) key;
                        if (valueToAmount.containsKey(numKey))
                            valueToAmount.put(numKey, valueToAmount.get(numKey) + 1);
                        else
                            valueToAmount.put(numKey, 1);
                    } else if (key instanceof Boolean) {
                        Boolean boolKey = (Boolean) key;
                        if (valueToAmount.containsKey(boolKey))
                            valueToAmount.put(boolKey, valueToAmount.get(boolKey) + 1);
                        else
                            valueToAmount.put(boolKey, 1);
                    } else if (key instanceof String) {
                        String strKey = (String) key;
                        if (valueToAmount.containsKey(strKey))
                            valueToAmount.put(strKey, valueToAmount.get(strKey) + 1);
                        else
                            valueToAmount.put(strKey, 1);
                    }
                }
            }
        }
        return new HistogramDTO(valueToAmount);
    }

    @Override
    public Double getConsistency(Integer id, String entityName, String propertyName) {
        EntityDefinition entityDefinition = simulationManager.getSpecificWorld(id).getNameToEntityDefinition().get(entityName);
        List<Double> consistency = new ArrayList<>();
        for (EntityInstance instance : entityDefinition.getEntityInstances()) {
            consistency.add(instance.getPropertyByName(propertyName).getAvgUpdateTicks());
        }
        if (consistency.size() != 0)
            return consistency.stream().mapToDouble(Double::doubleValue).sum() / consistency.size();
        else
            return 0d;
    }


    @Override
    public MeanPropertyDTO getMeanOfProperty(Integer id, String entityName, String propertyName) throws EntityPropertyNotExistException {
        EntityDefinition entityDefinition = simulationManager.getSpecificWorld(id).getNameToEntityDefinition().get(entityName);
        AbstractPropertyDefinition.PropertyType type = entityDefinition.getPropertyByName(propertyName).getType();
        Double mean = null;

        if (type.equals(AbstractPropertyDefinition.PropertyType.BOOLEAN) || type.equals(AbstractPropertyDefinition.PropertyType.STRING))
            return new MeanPropertyDTO(false, null, "The property is of type " + type.name() + ", hence not numerical. Can't calculate mean");

        if (entityDefinition.getEntityInstances().size() == 0)
            return new MeanPropertyDTO(false, null, "There are no instances of this entity, hence it's impossible to calculate the mean");

        if (type.equals(AbstractPropertyDefinition.PropertyType.DECIMAL)) {
            mean = entityDefinition.getEntityInstances().stream()
                    .mapToInt((entityInstance) -> (int) entityInstance.getPropertyByName(propertyName).getValue())
                    .sum() / (double) entityDefinition.getEntityInstances().size();
        }

        else if (type.equals(AbstractPropertyDefinition.PropertyType.FLOAT)) {
            mean = entityDefinition.getEntityInstances().stream()
                    .mapToDouble((entityInstance) -> (float) entityInstance.getPropertyByName(propertyName).getValue())
                    .sum() / (double) entityDefinition.getEntityInstances().size();
        }

        return new MeanPropertyDTO(true, mean,null);
    }

    @Override
    public Map<Integer, Boolean> getAllSimulationsStatus() {
        Map<Integer, Boolean> allSimulationsStatus = new HashMap<>();
        for (Integer key : simulationManager.getAllSimulationsID()) {
            allSimulationsStatus.put(key, simulationManager.getSpecificWorld(key).getPastSimulation().isRunning());
        }
        return allSimulationsStatus;
    }

    @Override
    public void pauseSimulation(Integer id) {
        simulationManager.getSpecificWorld(id).pauseSimulation();
    }

    @Override
    public void resumeSimulation(Integer id) {
        simulationManager.getSpecificWorld(id).resumeSimulation();
    }

    @Override
    public void stopSimulation(Integer id) {
        simulationManager.getSpecificWorld(id).stopSimulation();
    }

}
