package dto;

public class ConditionActionDTO {
    private final boolean singular;
    private final Integer thenActions;
    private final Integer elseActions;
    private final Integer numOfSubConditions;

    public ConditionActionDTO(boolean singular, Integer thenActions, Integer elseActions, Integer numOfSubConditions) {
        this.singular = singular;
        this.thenActions = thenActions;
        this.elseActions = elseActions;
        this.numOfSubConditions = numOfSubConditions;
    }

    public boolean isSingular() {
        return singular;
    }

    public Integer getThenActions() {
        return thenActions;
    }

    public Integer getElseActions() {
        return elseActions;
    }

    public Integer getNumOfSubConditions() {
        return numOfSubConditions;
    }
}
