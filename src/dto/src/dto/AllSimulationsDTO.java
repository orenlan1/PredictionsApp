package dto;

import java.util.List;

public class AllSimulationsDTO {
    private final List<PastSimulationDTO> simulationsList;

    public AllSimulationsDTO(List<PastSimulationDTO> list) {
        this.simulationsList = list;
    }

    public List<PastSimulationDTO> getSimulationsList() {
        return simulationsList;
    }
}
