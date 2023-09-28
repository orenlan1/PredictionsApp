package dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PastSimulationDTO {
    private final int id;
    private final List<PastEntityDTO> entitiesDTO;
    private final Map<String, Map<Integer, Integer>> entityToPopulation;
    private final Map<String, Integer> dynamicPopulation;
    private final List<EnvVariablesDTO> environmentVariables;
    private boolean running;
    private final Integer ticks;
    private final Integer seconds;

    public PastSimulationDTO(int simulationID, List<PastEntityDTO> entitiesDTO, Map<String, Map<Integer, Integer>> entityToPopulation,
                             Map<String, Integer> dynamicPopulation, List<EnvVariablesDTO> environmentVariables, boolean running, Integer ticks, Integer seconds) {
        this.id = simulationID;
        this.entitiesDTO = entitiesDTO;
        this.dynamicPopulation = dynamicPopulation;
        this.entityToPopulation = entityToPopulation;
        this.environmentVariables = environmentVariables;
        this.running = running;
        this.ticks = ticks;
        this.seconds = seconds;
    }

    public int getId() {
        return id;
    }

    public List<PastEntityDTO> getEntitiesDTO() {
        return entitiesDTO;
    }

    public Map<String, Integer> getDynamicPopulation() { return dynamicPopulation; }

    public Map<String, Map<Integer, Integer>> getEntityToPopulation() {
        return entityToPopulation;
    }

    public List<EnvVariablesDTO> getEnvironmentVariables() {
        return environmentVariables;
    }

    public boolean isRunning() { return running; }

    public Integer getTicks() { return ticks; }

    public Integer getSeconds() { return seconds; }
}
