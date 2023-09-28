package world;

import java.io.*;
import java.util.*;

import dto.EntityInitializationDTO;
import world.action.api.Action;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.environment.api.ActiveEnvironment;
import world.environment.api.EnvironmentVariablesManager;
import world.environment.impl.ActiveEnvironmentImpl;
import world.exceptions.RuleNameExistException;
import world.grid.Grid;
import world.rule.api.Rule;
import world.simulation.PastSimulation;
import world.termination.Termination;

public class World implements Serializable {
    private final Map<String, EntityDefinition> nameToEntityDefinition;
    private EnvironmentVariablesManager environmentVariablesManager;
    private ActiveEnvironment activeEnvironment;
    private final List<Rule> rules;
    private int ticks;
    private int population;
    private int simulationID = 0;
    private Termination termination;
    private PastSimulation pastSimulation;
    private Grid grid;
    private int threadCount;
    private boolean paused;
    private boolean stopped;
    private final Grid pauseLock;

    public World() {
        ticks = 0;
        population = 0;
        nameToEntityDefinition = new HashMap<>();
        rules = new ArrayList<>();
        pastSimulation = new PastSimulation(new LinkedList<>(), 0, new Date(),new HashMap<>(), new HashMap<>(), new ActiveEnvironmentImpl());
        paused = false;
        stopped = false;
        pauseLock = new Grid(0, 0);
    }

    public int getTotalPopulation() {
        return population;
    }

    public void addEntityDefinition(String name, EntityDefinition entityDefinition) {
        nameToEntityDefinition.put(name, entityDefinition);
        population += entityDefinition.getPopulation();
    }

    public void updatePopulation(int n) {
        population = n;
    }

    public Optional<EntityDefinition> getEntityDefinitionByName(String name) {
        return Optional.ofNullable(nameToEntityDefinition.get(name));
    }

    public EnvironmentVariablesManager getEnvironmentVariablesManager() {
        return environmentVariablesManager;
    }

    public void setEnvironmentVariablesManager(EnvironmentVariablesManager environmentVariablesManager) {
        this.environmentVariablesManager = environmentVariablesManager;
    }

    public void setActiveEnvironment(ActiveEnvironment activeEnvironment) {
        this.activeEnvironment = activeEnvironment;
    }

    public Collection<EntityDefinition> getEntityDefinitions() {
        return nameToEntityDefinition.values();
    }

    public ActiveEnvironment getActiveEnvironment() {
        return activeEnvironment;
    }

    public int getTicks() {
        return ticks;
    }

    public void tick() {
        ticks++;
    }

    public void resetTicks() {
        ticks = 0;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void addRule(Rule rule) throws RuleNameExistException {
        for (Rule existingRule : rules) {
            if (existingRule.getName().equals(rule.getName())) {
                throw new RuleNameExistException(rule.getName());
            }
        }
        rules.add(rule);
    }

    public void setEntitiesPopulation(List<EntityInitializationDTO> DTOs) {
        for (EntityInitializationDTO dto : DTOs) {
            nameToEntityDefinition.get(dto.getName()).setPopulation(dto.getPopulation());
        }
    }

    public Map<String, EntityDefinition> getNameToEntityDefinition() {
        return nameToEntityDefinition;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public void setSimulationID(int id) { this.simulationID = id; }

    public int getSimulationID() {
        return simulationID;
    }

    public void setPastSimulation(PastSimulation pastSimulation) {
        this.pastSimulation = (pastSimulation);
    }

    public PastSimulation getPastSimulation() {
        return pastSimulation;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public void moveAllEntitiesCoordinates(Grid grid) {
        for (EntityDefinition entityDefinition : this.getEntityDefinitions()) {
            for (EntityInstance entityInstance : entityDefinition.getEntityInstances()) {
                entityInstance.moveEntityCoordinate(grid);
            }
        }
    }

    public List<Rule> computeActiveRules(int tick) {
        List<Rule> activeRules = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.getActivation().isActive(tick)) {
                activeRules.add(rule);
            }
        }
        return activeRules;
    }

    public List<Action> getActiveRulesActions(List<Rule> activeRules) {
        List<Action> actionList = new ArrayList<>();
        for (Rule rule : activeRules) {
            actionList.addAll(rule.getaActionsToPerform());
        }
        return actionList;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public World deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (World) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;

        }
    }

    public void stopSimulation() {
        stopped = true;
        synchronized (pauseLock) {
            pauseLock.notifyAll();
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public Object getPauseLock() {
        return pauseLock;
    }

    public void pauseSimulation() {
        paused = true;
    }

    public void resumeSimulation() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
