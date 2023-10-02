package dto;

import java.util.List;

public class SimulationInfoDTO {
    private final List<EntityDTO> entitiesList;
    private final List<RuleDTO> rulesList;
    private final List<PropertyDTO> envVariablesList;
    private final GridDTO grid;

    public SimulationInfoDTO(List<EntityDTO> entitiesList, List<RuleDTO> rulesList, List<PropertyDTO> envVariablesList, GridDTO grid) {
        this.entitiesList = entitiesList;
        this.rulesList = rulesList;
        this.envVariablesList = envVariablesList;
        this.grid = grid;
    }
    public List<EntityDTO> getEntitiesList() {
        return entitiesList;
    }

    public List<RuleDTO> getRulesList() {
        return rulesList;
    }

    public List<PropertyDTO> getEnvVariablesList() {return envVariablesList; }

    public GridDTO getGrid() {
        return grid;
    }
}

