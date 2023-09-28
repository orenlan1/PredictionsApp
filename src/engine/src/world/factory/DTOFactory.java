package world.factory;

import dto.*;
import world.action.api.ActionType;
import world.action.impl.MultipleCondition;
import world.action.impl.SingularCondition;
import world.property.api.PropertyInstance;
import world.termination.Termination;
import world.action.api.Action;
import world.entity.api.EntityDefinition;
import world.property.api.PropertyDefinition;
import world.property.impl.FloatPropertyDefinition;
import world.property.impl.IntegerPropertyDefinition;
import world.rule.api.Rule;

import java.util.ArrayList;
import java.util.List;

public class DTOFactory {

    public DTOFactory() {
    }

    public PropertyDTO createPropertyDTO(PropertyDefinition propertyDefinition) {
        Double from = null;
        Double to = null;
        if (propertyDefinition instanceof IntegerPropertyDefinition) {
            from = Double.valueOf(((IntegerPropertyDefinition) propertyDefinition).getFrom());
            to = Double.valueOf(((IntegerPropertyDefinition) propertyDefinition).getTo());
        } else if (propertyDefinition instanceof FloatPropertyDefinition) {
            from = Double.valueOf(((FloatPropertyDefinition) propertyDefinition).getFrom());
            to = Double.valueOf(((FloatPropertyDefinition) propertyDefinition).getTo());
        }
        return new PropertyDTO(propertyDefinition.getName(),propertyDefinition.getType().name().toLowerCase(),from,to,propertyDefinition.getRandomInitialize());
    }

    public EntityDTO createEntityDTO(EntityDefinition entityDefinition) {
        List<PropertyDTO> propertyDTOList = new ArrayList<>();
        for ( PropertyDefinition propertyDefinition : entityDefinition.getPropertiesList()) {
            propertyDTOList.add(createPropertyDTO(propertyDefinition));
        }
        return new EntityDTO(entityDefinition.getName(),entityDefinition.getPopulation(), propertyDTOList);
    }

    public RuleDTO creatRuleDTO(Rule rule) {
        List<ActionDTO> actionsDTO = new ArrayList<>();
        for (Action action : rule.getaActionsToPerform()) {
            actionsDTO.add(createActionDTO(action));
        }
        return new RuleDTO(rule.getName(),rule.getActivation().getTicks(),rule.getActivation().getProbability(), actionsDTO.size(), actionsDTO);
    }


    public ActionDTO createActionDTO(Action action) {
        String secondaryEntityName = null;
        EntityDefinition secondaryEntity = action.getSecondaryEntityDefinition();
        if (secondaryEntity != null)
            secondaryEntityName = secondaryEntity.getName();
        if (action.getActionType().equals(ActionType.CONDITION)) {
            if (action instanceof SingularCondition) {
                SingularCondition condition = (SingularCondition) action;
                ConditionActionDTO conditionActionDTO = new ConditionActionDTO(Boolean.TRUE, condition.getNumThen(), condition.getNumElse(), null);
                return new ActionDTO(action.getActionType().toString(), action.getMainEntityDefinition().getName(), secondaryEntityName, Boolean.FALSE, action.getArguments(), conditionActionDTO);
            } else {
                MultipleCondition condition = (MultipleCondition) action;
                ConditionActionDTO conditionActionDTO = new ConditionActionDTO(Boolean.FALSE, condition.getNumThen(), condition.getNumElse(), condition.getNumOfSubConditions());
                return new ActionDTO(action.getActionType().toString(), action.getMainEntityDefinition().getName(), secondaryEntityName, Boolean.FALSE, action.getArguments(), conditionActionDTO);
            }
        }
        else
            return new ActionDTO(action.getActionType().toString(), action.getMainEntityDefinition().getName(), secondaryEntityName, Boolean.FALSE, action.getArguments(), null);
    }

    public TerminationDTO createTerminationDTO(Termination termination) {
        return new TerminationDTO(termination.getTicksCount(), termination.getSecondCount());
    }

    public EnvVariablesDTO createEnvVariableDTO(PropertyInstance envVariable) {
        return new EnvVariablesDTO(envVariable.getPropertyDefinition().getName(), envVariable.getValue().toString());
    }
}
