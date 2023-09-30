package world.simulation;

import world.World;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimulationExecutionManager {

    private Map<String, Map<Integer, World>> nameToIdToWorld;
    ExecutorService threadExecutor;
    private int simulationId = 0;


    public SimulationExecutionManager() {
        this.nameToIdToWorld = new LinkedHashMap<>();
        this.threadExecutor = Executors.newFixedThreadPool(1);
    }

    public void setThreadCount(int threadCount) {
        threadExecutor = Executors.newFixedThreadPool(threadCount);
    }

    public World getMainWorld(String name) {
        return nameToIdToWorld.get(name).get(0);
    }

    public ExecutorService getThreadExecutor() {
        return threadExecutor;
    }

    public void addWorldSimulation(String name, World newWorld, boolean prime) {
        Map<Integer, World> idToWorld = new LinkedHashMap<>();
        int evaluatedSimulationID = prime ? 0 : ++simulationId;
        newWorld.setSimulationID(evaluatedSimulationID);
        idToWorld.put(evaluatedSimulationID, newWorld);
        nameToIdToWorld.put(name, idToWorld);
    }

    public World getSpecificWorld(String name, Integer id) {
        return nameToIdToWorld.get(name).get(id);
    }

    //TODO - fix this function according to iur future need of it
    public Collection<String> getAllSimulationNames() {
        return nameToIdToWorld.keySet();
    }
}