package world.simulation;

import world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationExecutionManager {

    private Map<Integer, World> idToWorld;
    ExecutorService threadExecutor;
    private int simulationId = 0;


    public SimulationExecutionManager(int threadCount) {
        this.idToWorld = new LinkedHashMap<>();
        this.threadExecutor = Executors.newFixedThreadPool(threadCount);
    }

    public World getMainWorld() {
        return idToWorld.get(0);
    }

    public ExecutorService getThreadExecutor() {
        return threadExecutor;
    }

    public void addWorldSimulation(World newWorld) {
        newWorld.setSimulationID(simulationId);
        this.idToWorld.put(simulationId++, newWorld);
    }

    public World getSpecificWorld(Integer id) {
        return idToWorld.getOrDefault(id, null);
    }

    public Collection<Integer> getAllSimulationsID() { return idToWorld.keySet(); }
}