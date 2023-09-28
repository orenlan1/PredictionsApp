package world.simulation;

import world.entity.api.EntityDefinition;
import world.environment.api.ActiveEnvironment;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PastSimulation implements Serializable {
    private final Collection<EntityDefinition> entities;
    private final int id;
    private final Date date;
    private final Map<String , Map<Integer, Integer>> entityToPopulation;
    private final Map<String, Integer> dynamicPopulation;
    private final ActiveEnvironment activeEnvironment;
    private boolean running;
    private Integer ticks;
    private Integer seconds;

    public PastSimulation(Collection<EntityDefinition> entityDefinitions, int simulationID, Date date, Map<String , Map<Integer, Integer>> entityToPopulation,
                          Map<String, Integer> dynamicPopulation, ActiveEnvironment activeEnvironment) {
        entities = entityDefinitions;
        id = simulationID;
        this.date = date;
        this.entityToPopulation = entityToPopulation;
        this.activeEnvironment = activeEnvironment;
        this.dynamicPopulation = dynamicPopulation;
        running = true;
        ticks = 0;
        seconds = 0;
    }

    public Collection<EntityDefinition> getEntities() {
        return entities;
    }

    public int getSimulationId() {
        return id;
    }

    public Date getSimulationDate() {
        return date;
    }

    public Map<String, Map<Integer, Integer>> getEntityToPopulation() {
        return entityToPopulation;
    }

    public ActiveEnvironment getActiveEnvironment() {
        return activeEnvironment;
    }

    public boolean isRunning() { return running; }

    public void setRunning(boolean running) { this.running = running; }

    public void setTicks(Integer ticks) { this.ticks = ticks; }

    public void setSeconds(Integer seconds) { this.seconds = seconds; }

    public Integer getTicks() { return ticks; }

    public Integer getSeconds() { return seconds; }

    public Map<String, Integer> getDynamicPopulation() { return dynamicPopulation; }
}
