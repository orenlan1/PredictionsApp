package world.simulation;

import components.queue.management.ThreadPoolDelegate;
import dto.SimulationRunnerDTO;
import world.World;
import world.action.api.Action;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.environment.api.ActiveEnvironment;
import world.property.api.PropertyDefinition;
import world.property.api.PropertyInstance;
import world.property.impl.PropertyInstanceImpl;
import world.rule.api.Rule;

import java.util.*;

public class SimulationExecutor implements Runnable {
    private World world;
    private SimulationRunnerDTO simulationRunnerDTO;
    private ThreadPoolDelegate threadPoolDelegate;

    public SimulationExecutor(ThreadPoolDelegate threadPoolDelegate) {
        this.threadPoolDelegate = threadPoolDelegate;
    }

    public void setWorld(World world) { this.world = world; }

    public SimulationRunnerDTO getSimulationRunnerDTO() { return simulationRunnerDTO; }

    @Override
    public void run() {
        threadPoolDelegate.decreaseSimulationsInQueue();
        threadPoolDelegate.increaseRunningSimulations();
        Integer seconds = world.getTermination().getSecondCount();
        Integer ticks = world.getTermination().getTicksCount();
        world.resetTicks();
        try {
            simulationRulesPerform(world, seconds, ticks);
        } catch (Exception e) {
            simulationRunnerDTO = new SimulationRunnerDTO(Boolean.FALSE, e.getMessage(), world.getSimulationID(), Boolean.FALSE);
        } finally {
            world.getPastSimulation().setRunning(false);
            threadPoolDelegate.decreaseRunningSimulations();
            threadPoolDelegate.increaseFinishedSimulations(world.getSimulationID());
        }
        if (ticks != null && seconds != null) {
            if (world.getTicks() >= ticks)
                simulationRunnerDTO = new SimulationRunnerDTO(Boolean.TRUE, null, world.getSimulationID(), Boolean.TRUE);
            else
                simulationRunnerDTO = new SimulationRunnerDTO(Boolean.TRUE, null, world.getSimulationID(), Boolean.FALSE);
        }
        else if (ticks != null)
            simulationRunnerDTO = new SimulationRunnerDTO(Boolean.TRUE, null, world.getSimulationID(), Boolean.TRUE);
        else
            simulationRunnerDTO = new SimulationRunnerDTO(Boolean.TRUE, null, world.getSimulationID(), Boolean.FALSE);
    }

    public void simulationRulesPerform(World world, Integer seconds, Integer ticks) throws Exception {
        Thread currThread = Thread.currentThread();
        Map<String, Map<Integer, Integer>> entityToPopulation = new HashMap<>();
        Map<String, Integer> dynamicPopulation = new HashMap<>();
        for (EntityDefinition entityDefinition : world.getEntityDefinitions()) {
            entityDefinition.createEntityInstancesPopulation(world.getGrid());
            entityToPopulation.put(entityDefinition.getName(), new LinkedHashMap<>());
            Map<Integer, Integer> entityCount = entityToPopulation.get(entityDefinition.getName());
            entityCount.put(0, entityDefinition.getPopulation());
            dynamicPopulation.put(entityDefinition.getName(), entityDefinition.getPopulation());
        }

        Boolean valid = true;
        long start = System.currentTimeMillis();
        Date date = new Date(start);
        world.setPastSimulation(new PastSimulation(world.getEntityDefinitions(), world.getSimulationID(), date, entityToPopulation, dynamicPopulation, world.getActiveEnvironment()));

        while (valid) {
            if (world.isPaused()) {
                synchronized (world.getPauseLock()) {
                    while (world.isPaused()) {
                        try {
                            world.getPauseLock().wait();
                        } catch (InterruptedException e) {
                            throw new Exception(e);
                        }
                    }
                }
            }
            if (world.isStopped()) {
                valid = false;
                break;
            }
            world.moveAllEntitiesCoordinates(world.getGrid());
            for (EntityDefinition entityDefinition : world.getEntityDefinitions()) {
                for (EntityInstance entityInstance : entityDefinition.getEntityInstances()) {
                    List<Rule> activeRules = world.computeActiveRules(world.getTicks());
                    if (!activeRules.isEmpty()) {
                        List<Action> actionsList = world.getActiveRulesActions(activeRules);
                        for (Action action : actionsList) {
                            if (action.getMainEntityDefinition() == entityDefinition) {
                                if (entityInstance.isAlive()) {
                                    if (action.getSecondaryEntityComponent() != null) {
                                        List<EntityInstance> secondaryEntityInstanceList = action.getSecondaryEntityComponent().computeSecondaryEntitiesForAction(world.getTicks());
                                        for (EntityInstance secondaryEntityInstance : secondaryEntityInstanceList) {
                                            action.activate(world.getTicks(), entityInstance, secondaryEntityInstance);
                                        }
                                    } else {
                                        action.activate(world.getTicks(), entityInstance);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            world.tick();
            world.getPastSimulation().setTicks(world.getTicks());
            world.getPastSimulation().setSeconds((int)(System.currentTimeMillis() - start) / 1000);

            for (EntityDefinition entDefinition : world.getEntityDefinitions()) {
                List<EntityInstance> entitiesToReplace = new ArrayList<>();
                for (EntityInstance instance : entDefinition.getEntityInstances()) {
                    if (instance.isToReplace()) {
                        entitiesToReplace.add(instance);
                    }
                }
                for (EntityInstance instance : entitiesToReplace) {
                    Action action = instance.getReplaceAction();
                    action.activate(world.getTicks(), instance, null);
                }
            }

            int sumPopulation = 0;
            for (EntityDefinition entityDefinition : world.getEntityDefinitions()) {
                List<EntityInstance> entityInstances = entityDefinition.getEntityInstances();
                entityInstances.removeIf(entityInstance -> !entityInstance.isAlive());
                entityDefinition.setPopulation(entityInstances.size());
                sumPopulation += entityInstances.size();
                dynamicPopulation.put(entityDefinition.getName(), entityInstances.size());
            }
            world.updatePopulation(sumPopulation);

            if (world.getTicks() % 1000 == 0) {
                for (EntityDefinition entityDefinition : world.getEntityDefinitions()) {
                    Map<Integer, Integer> entityCount = entityToPopulation.get(entityDefinition.getName());
                    entityCount.put(world.getTicks(), entityDefinition.getPopulation());
                }
            }

            if (ticks != null && seconds != null)
                valid = world.getTicks() < ticks && System.currentTimeMillis() - start < (seconds * 1000L);
            else if (ticks != null)
                valid = world.getTicks() < ticks;
            else if (seconds != null)
                valid = System.currentTimeMillis() - start < (seconds * 1000L);

            //TODO - add user terminated
        }

        for (EntityDefinition entityDefinition : world.getEntityDefinitions()) {
            for (EntityInstance instance : entityDefinition.getEntityInstances()) {
                for (PropertyInstance propertyInstance : instance.getPropertyInstances())
                    propertyInstance.addLastTick(world.getTicks());
            }
        }
    }
}
