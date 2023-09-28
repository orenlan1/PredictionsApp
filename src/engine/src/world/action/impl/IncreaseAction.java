package world.action.impl;

import world.action.api.ActionType;
import world.context.Context;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.expressions.api.Expression;
import world.expressions.impl.HelperFunctionExpression;
import world.helper.function.impl.PercentFunction;
import world.property.api.AbstractPropertyDefinition;
import world.property.api.PropertyDefinition;
import world.property.api.PropertyInstance;
import world.property.impl.FloatPropertyDefinition;
import world.property.impl.IntegerPropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class IncreaseAction extends ActionImpl {
    private final Expression by;


    public IncreaseAction(EntityDefinition entityDefinition, Expression expression, PropertyDefinition propertyDefinition, SecondaryEntity secondaryEntity, Context entitiesContext) {
        super(ActionType.INCREASE, entityDefinition, propertyDefinition, secondaryEntity, entitiesContext);
        this.by = expression;
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
        try {
            Object value = null;
            if (by instanceof HelperFunctionExpression) {
                HelperFunctionExpression helperBy = (HelperFunctionExpression) by;
                if (helperBy.getHelperFunction() instanceof PercentFunction) {
                    PercentFunction percentFunction = (PercentFunction) helperBy.getHelperFunction();
                    if ( entityInstance.length >=2)
                        value = percentFunction.percentInvoke(entityInstance[0], entityInstance[1], currTick);
                    else
                        value = percentFunction.percentInvoke(entityInstance[0], null, currTick);
                }
                else {
                    try {
                        value = helperBy.evaluate(entityInstance[0], currTick);
                    } catch (Exception e) {
                        if (entityInstance.length >= 2)
                            value = helperBy.evaluate(entityInstance[1], currTick);
                    }
                }
            } else
                value = by.evaluate(entityInstance[0]);
            Object newValue = null;

            if (propertyDefinition.getType().equals(AbstractPropertyDefinition.PropertyType.DECIMAL)) {
                if (value instanceof Integer) {
                    IntegerPropertyDefinition intPropertyDef = (IntegerPropertyDefinition) propertyDefinition;
                    int to = intPropertyDef.getTo();
                    newValue = (Integer) property.getValue() + (Integer) value;
                    if ((Integer) newValue > to) {
                        return;
                    }


                }
            }
            else if (propertyDefinition.getType().equals(AbstractPropertyDefinition.PropertyType.FLOAT)) {
                if (value instanceof Float) {
                    FloatPropertyDefinition floatPropertyDef = (FloatPropertyDefinition) propertyDefinition;
                    float to = floatPropertyDef.getTo();
                    newValue = (float) property.getValue() + (float) value;
                    if ((float) newValue > to) {
                        return;
                    }
                }
            }
            if (newValue != null) {
                property.updateValue(newValue, currTick);
            }
        }
        catch (NumberFormatException e) {
            throw new Exception("Invalid expression, expected " + propertyDefinition.getType().toString());
        }
    }

    @Override
    public List<String> getArguments() {
        List<String> args =  new ArrayList<>();
        args.add(by.toString());
        return args;
    }
}
