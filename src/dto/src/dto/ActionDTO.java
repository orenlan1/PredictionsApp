package dto;

import java.util.List;

public class ActionDTO {
    private final String type;
    private final String primaryEntity;
    private final String secondaryEntity;
    private final boolean isSecondaryEntity;
    private final List<String> args;
    private final ConditionActionDTO conditionActionDTO;

    public ActionDTO(String type, String primaryEntity, String secondaryEntity, boolean isSecondaryEntity, List<String> args, ConditionActionDTO conditionActionDTO) {
        this.type = type;
        this.primaryEntity = primaryEntity;
        this.secondaryEntity = secondaryEntity;
        this.isSecondaryEntity = isSecondaryEntity;
        this.args = args;
        this.conditionActionDTO = conditionActionDTO;
    }

    public String getType() {
        return type;
    }

    public String getPrimaryEntity() {
        return primaryEntity;
    }

    public String getSecondaryEntity() {
        return secondaryEntity;
    }

    public boolean isSecondaryEntity() {
        return isSecondaryEntity;
    }

    public List<String> getArgs() {
        return args;
    }

    public ConditionActionDTO getConditionActionDTO() {
        return conditionActionDTO;
    }
}

