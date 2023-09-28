package world.action.impl;

import world.action.api.Action;
import world.context.Context;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.exceptions.InvalidConditionOperatorException;
import world.exceptions.InvalidVariableTypeException;
import world.expressions.api.Expression;
import world.expressions.impl.HelperFunctionExpression;
import world.helper.function.impl.PercentFunction;
import world.property.api.PropertyDefinition;
import world.property.api.PropertyInstance;

import java.util.ArrayList;
import java.util.List;

public class SingularCondition extends ConditionAction {
    private final Expression property;
    private final Expression value;
    private final String operator;

    public SingularCondition(EntityDefinition entityDefinition, Expression property, Expression value, String operator, List<Action> thenActions, List<Action> elseActions, SecondaryEntity secondaryEntity, Context entitiesContext) {
        super(entityDefinition, thenActions, elseActions, secondaryEntity, entitiesContext);
        this.value = value;
        this.property = property;
        this.operator = operator;
    }

    @Override
    public boolean evaluate(int currTick, EntityInstance... entityInstance) throws Exception, InvalidConditionOperatorException, InvalidVariableTypeException {

        Object propertyValue = null;

        if (property instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperPropertyValue = (HelperFunctionExpression) property;
            if (helperPropertyValue.getHelperFunction() instanceof PercentFunction) {
                PercentFunction percentFunction = (PercentFunction) helperPropertyValue.getHelperFunction();
                if ( entityInstance.length >=2)
                    propertyValue = percentFunction.percentInvoke(entityInstance[0], entityInstance[1], currTick);
                else
                    propertyValue = percentFunction.percentInvoke(entityInstance[0], null, currTick);
            }
            else {
                try {
                    propertyValue = helperPropertyValue.evaluate(entityInstance[0], currTick);
                } catch (Exception e) {
                    if (entityInstance.length >= 2)
                        propertyValue = helperPropertyValue.evaluate(entityInstance[1], currTick);
                }
            }
        } else {
            propertyValue = property.evaluate(entityInstance[0]);
        }
        String propType = this.property.getType();

        Object expValue = null;
        if (value instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperValue = (HelperFunctionExpression) value;
            if (helperValue.getHelperFunction() instanceof PercentFunction) {
                PercentFunction percentFunction = (PercentFunction) helperValue.getHelperFunction();
                if ( entityInstance.length >=2)
                    propertyValue = percentFunction.percentInvoke(entityInstance[0], entityInstance[1], currTick);
                else
                    propertyValue = percentFunction.percentInvoke(entityInstance[0], null, currTick);
            }
            else {
                try {
                    expValue = helperValue.evaluate(entityInstance[0], currTick);
                } catch (Exception e) {
                    if (entityInstance.length >= 2)
                        propertyValue = helperValue.evaluate(entityInstance[1], currTick);
                }
            }
        } else
            expValue = value.evaluate(entityInstance[0]);

        String expType = value.getType();

        switch (operator) {
            case "=":
                if (expType.equals("string"))
                    return ((String) propertyValue).equals((String) expValue);
                else
                    return propertyValue == expValue;
            case "!=":
                if (expType.equals("string"))
                    return !((String) propertyValue).equals((String) expValue);
                else
                    return propertyValue != expValue;
            case "bt":
                if (expType.equals("string") || expType.equals("boolean"))
                    throw new InvalidVariableTypeException("evaluating a condition", "decimal or float", expType);
                else if (expType.equals("decimal") && propType.equals("decimal"))
                    return ((int) propertyValue) > ((int) expValue);
                else if (expType.equals("float") && propType.equals("decimal"))
                    return ((int) propertyValue) > ((float) expValue);
                else if (expType.equals("decimal") && propType.equals("float"))
                    return ((float) propertyValue) > ((int) expValue);
                else if (expType.equals("float") && propType.equals("float"))
                    return ((float) propertyValue) > ((float) expValue);
            case "lt":
                if (expType.equals("string") || expType.equals("boolean"))
                    throw new InvalidVariableTypeException("evaluating a condition", "decimal or float", expType);
                else if (expType.equals("decimal") && propType.equals("decimal"))
                    return ((int) propertyValue) < ((int) expValue);
                else if (expType.equals("float") && propType.equals("decimal"))
                    return ((int) propertyValue) < ((float) expValue);
                else if (expType.equals("decimal") && propType.equals("float"))
                    return ((float) propertyValue) < ((int) expValue);
                else if (expType.equals("float") && propType.equals("float"))
                    return ((float) propertyValue) < ((float) expValue);
            default:
                throw new InvalidConditionOperatorException(operator);
        }
    }

    @Override
    public List<String> getArguments() {
        List<String> args =  new ArrayList<>();
        args.add(value.toString());
        args.add(operator);
        args.add(propertyDefinition.getName());
        return args;
    }
}


