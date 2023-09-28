package world.action.impl;

import world.action.api.Action;
import world.context.Context;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.exceptions.InvalidConditionOperatorException;
import world.exceptions.InvalidVariableTypeException;
import world.property.api.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class MultipleCondition extends ConditionAction {
    private final List<ConditionAction> conditions;
    private final String logic;
    private final Integer numOfSubConditions;

    public MultipleCondition(List<ConditionAction> conditions, String logic, EntityDefinition entityDefinition, List<Action> thenActions, List<Action> elseActions, SecondaryEntity secondaryEntity, Context entitiesContext) {
        super(entityDefinition, thenActions, elseActions, secondaryEntity, entitiesContext);
        this.conditions = conditions;
        this.logic = logic;
        this.numOfSubConditions = conditions.size();
    }

    @Override
    public boolean evaluate(int currTick, EntityInstance... entityInstance) throws Exception, InvalidConditionOperatorException, InvalidVariableTypeException {
        boolean conditionRes = false, tempRes;

        if (logic.equals("and"))
            conditionRes = true;

        for (ConditionAction condition : conditions) {
            tempRes = condition.evaluate(currTick, entityInstance);

            if (logic.equals("or"))
                conditionRes = conditionRes || tempRes;
            else
                conditionRes = conditionRes && tempRes;
        }
        return conditionRes;
    }

    public Integer getNumOfSubConditions() {
        return numOfSubConditions;
    }

    @Override
    public String toString() { return "MULTIPLE CONDITION action"; }

    @Override
    public List<String> getArguments() {
        List<String> args =  new ArrayList<>();
        args.add(logic);
        return args;
    }
}
