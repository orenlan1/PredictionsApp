package world.action.impl;

import world.action.api.ActionType;
import world.context.Context;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.exceptions.InvalidVariableTypeException;
import world.exceptions.MismatchTypesException;
import world.expressions.api.Expression;
import world.expressions.impl.HelperFunctionExpression;
import world.helper.function.impl.PercentFunction;
import world.property.api.AbstractPropertyDefinition;
import world.property.api.PropertyDefinition;
import world.property.api.PropertyInstance;

public class DivisionAction extends CalculationAction {

    public DivisionAction(EntityDefinition entityDefinition, PropertyDefinition propertyDefinition, Expression arg1, Expression arg2, SecondaryEntity secondaryEntity, Context entitiesContext) {
        super(ActionType.DIVISION, entityDefinition, propertyDefinition, arg1, arg2, secondaryEntity, entitiesContext);
    }

    @Override
    public void activate(int currTick, EntityInstance... entityInstance) throws Exception {
        PropertyInstance property = null;
        if (entityInstance[0].getEntityDefinition() == entityDefinition) {
            property = entityInstance[0].getPropertyByName(propertyDefinition.getName());
        }
        else {
            property = entityInstance[1].getPropertyByName(propertyDefinition.getName());
        }
        AbstractPropertyDefinition.PropertyType type = propertyDefinition.getType();

        Object arg1Value = null;
        Object arg2Value = null;

        if (arg1 instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperArg1 = (HelperFunctionExpression) arg1;
            if (helperArg1.getHelperFunction() instanceof PercentFunction) {
                PercentFunction percentFunction = (PercentFunction) helperArg1.getHelperFunction();
                if ( entityInstance.length >=2)
                    arg1Value = percentFunction.percentInvoke(entityInstance[0], entityInstance[1], currTick);
                else
                    arg1Value = percentFunction.percentInvoke(entityInstance[0], null, currTick);
            }
            else {
                try {
                    arg1Value = helperArg1.evaluate(entityInstance[0], currTick);
                } catch (Exception e) {
                    if (entityInstance.length >= 2)
                        arg1Value = helperArg1.evaluate(entityInstance[1], currTick);
                }
            }
        } else
            arg1Value = arg1.evaluate(entityInstance[0]);

        if (arg2 instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperArg2 = (HelperFunctionExpression) arg2;
            if (helperArg2.getHelperFunction() instanceof PercentFunction) {
                PercentFunction percentFunction = (PercentFunction) helperArg2.getHelperFunction();
                if ( entityInstance.length >=2)
                    arg1Value = percentFunction.percentInvoke(entityInstance[0], entityInstance[1], currTick);
                else
                    arg1Value = percentFunction.percentInvoke(entityInstance[0], null, currTick);
            }
            else {
                try {
                    arg2Value = helperArg2.evaluate(entityInstance[0], currTick);
                } catch (Exception e) {
                    if (entityInstance[1] != null)
                        arg2Value = helperArg2.evaluate(entityInstance[1], currTick);
                }
            }
        } else
            arg2Value = arg2.evaluate(entityInstance[0]);


        if (type.equals(AbstractPropertyDefinition.PropertyType.FLOAT)) {
            if ((arg1Value instanceof Float) || (arg1Value instanceof Integer) && ((arg2Value instanceof Float) || (arg2Value instanceof Integer))) {
                if ((float) arg2Value != 0) {
                    property.updateValue((float) arg1Value / (float) arg2Value, currTick);
                } else {
                    throw new ArithmeticException("division by zero");
                }
            } else {
                throw new MismatchTypesException("One or more of the arguments in division action", "Integer or Float", arg1Value.getClass().getTypeName() + ", " + arg2Value.getClass().getTypeName());
            }
        }

        if (type.equals(AbstractPropertyDefinition.PropertyType.DECIMAL)) {
            if ((arg1Value instanceof Float) || (arg1Value instanceof Integer) && ((arg2Value instanceof Float) || (arg2Value instanceof Integer))) {
                if ((float) arg2Value != 0) {
                    float newValue = (float) arg1Value / (float) arg2Value;
                    if (newValue - (int) newValue > 0) {
                        throw new InvalidVariableTypeException("performing division", "Integer", "a fraction");
                    } else
                        property.updateValue((int) newValue, currTick);
                } else {
                    throw new ArithmeticException("division by zero");
                }
            }
        }
    }
}
