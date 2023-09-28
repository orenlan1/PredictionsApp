package world.action.impl;

import world.action.api.Action;
import world.action.api.ActionType;
import world.context.Context;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.exceptions.InvalidConditionOperatorException;
import world.exceptions.InvalidVariableTypeException;
import world.expressions.api.Expression;
import world.property.api.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public abstract class ConditionAction extends ActionImpl {
    private final List<Action> thenActions;
    private final List<Action> elseActions;


    public ConditionAction(EntityDefinition entityDefinition, List<Action> thenActions, List<Action> elseActions, SecondaryEntity secondaryEntity, Context entitiesContext) {
        super(ActionType.CONDITION, entityDefinition, null, secondaryEntity, entitiesContext);
        this.thenActions = thenActions;
        this.elseActions = elseActions;
    }

    public abstract boolean evaluate(int currTick, EntityInstance... entityInstance) throws Exception;

    @Override
    public void activate(int currTick, EntityInstance... entityInstance) throws Exception {
        boolean conditionRes = evaluate(currTick, entityInstance);

        if (conditionRes) {
            for (Action action : thenActions) {
                if (action.getActionType().equals(ActionType.REPLACE)) {
                    entityInstance[0].setReplaceAction(action);
                    entityInstance[0].setToReplace(true);
                } else
                    action.activate(currTick, entityInstance);
            }
        } else {
            for (Action action : elseActions) {
                if (action.getActionType().equals(ActionType.REPLACE)) {
                    entityInstance[0].setReplaceAction(action);
                    entityInstance[0].setToReplace(true);
                } else
                    action.activate(currTick, entityInstance);
            }
        }
    }

    public Integer getNumThen() { return thenActions.size(); }

    public Integer getNumElse() {return elseActions.size(); }
}
