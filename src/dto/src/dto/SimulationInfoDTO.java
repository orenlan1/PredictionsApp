package dto;

import java.util.List;

public class SimulationInfoDTO {
    private final List<EntityDTO> entitiesList;
    private final List<RuleDTO> rulesList;
    private final TerminationDTO termination;

    public SimulationInfoDTO(List<EntityDTO> entitiesList, List<RuleDTO> rulesList, TerminationDTO termination) {
        this.entitiesList = entitiesList;
        this.rulesList = rulesList;
        this.termination = termination;
    }
    public List<EntityDTO> getEntitiesList() {
        return entitiesList;
    }


    public List<RuleDTO> getRulesList() {
        return rulesList;
    }

    public TerminationDTO getTermination() {
        return termination;
    }
}

