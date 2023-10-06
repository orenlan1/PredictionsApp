package predictions.api;

//import components.queue.management.ThreadPoolDelegate;
import dto.*;
import world.World;
import world.exceptions.EntityPropertyNotExistException;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PredictionsService {
    FileReaderDTO readFileAndLoad(InputStream fileContent);
    void setThreadCount(Integer threadCount);
    SimulationInfoDTO getSimulationInformation(String name);
    List<SimulationInfoDTO> getAllSimulationsInformation();

    boolean isAdminAlive();
    void setAdmin();
    void randomizeEnvProperties(String name);
    PropertiesDTO getEnvPropertiesDTO(String name);
    EnvVariableSetValidationDTO setEnvironmentVariables(String name, List<UserInputEnvironmentVariableDTO> DTOs);
    void setEntitiesPopulation(String name, List<EntityInitializationDTO> DTOs);
    List<EnvVariablesDTO> getEnvVariablesDTOList(String name, Integer id);
    //Integer runSimulation(ThreadPoolDelegate threadPoolDelegate);
    PastSimulationDTO getSimulationsDTO(String name, Integer id);
    HistogramDTO getHistogram(String name, Integer id, String entityName, String propertyName);
    Double getConsistency(String name, Integer id, String entityName, String propertyName);
    MeanPropertyDTO getMeanOfProperty(String name, Integer id, String entityName, String propertyName) throws EntityPropertyNotExistException;
    //Map<Integer, Boolean> getAllSimulationsStatus();
    void pauseSimulation(String name, Integer id);
    void resumeSimulation(String name, Integer id);
    void stopSimulation(String name, Integer id);
    Collection<String> getAllSimulationsNames();
}


