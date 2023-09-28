package world.action.impl;

import world.action.api.ActionType;
import world.context.Context;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.exceptions.InvalidVariableTypeException;
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

public class SetAction extends ActionImpl{

    private final Expression expression;

    public SetAction(EntityDefinition entityDefinition, Expression expression, PropertyDefinition propertyDefinition, SecondaryEntity secondaryEntity, Context entitiesContext) {
        super(ActionType.SET, entityDefinition, propertyDefinition, secondaryEntity, entitiesContext);
        this.expression = expression;
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
        AbstractPropertyDefinition.PropertyType type = property.getPropertyDefinition().getType();

        try {
            Object value = null;
            if (expression instanceof HelperFunctionExpression) {
                HelperFunctionExpression helperExpression = (HelperFunctionExpression) expression;
                if (helperExpression.getHelperFunction() instanceof PercentFunction) {
                    PercentFunction percentFunction = (PercentFunction) helperExpression.getHelperFunction();
                    if ( entityInstance.length >=2)
                        value = percentFunction.percentInvoke(entityInstance[0], entityInstance[1], currTick);
                    else
                        value = percentFunction.percentInvoke(entityInstance[0], null, currTick);
                }
                else {
                    try {
                        value = helperExpression.evaluate(entityInstance[0], currTick);
                    } catch (Exception e) {
                        if (entityInstance.length >= 2)
                            value = helperExpression.evaluate(entityInstance[1], currTick);
                    }
                }
            } else
                value = expression.evaluate(entityInstance[0]);


            if (type.equals(AbstractPropertyDefinition.PropertyType.DECIMAL)) {
                if ((value instanceof Integer)) {
                    IntegerPropertyDefinition intProperty = (IntegerPropertyDefinition) property.getPropertyDefinition();
                    Integer intValue = (Integer) value;
                    int from = intProperty.getFrom();
                    int to = intProperty.getTo();
                    if (intValue >= from && intValue <= to)
                        property.updateValue(intValue, currTick);
                }
            } else if (type.equals(AbstractPropertyDefinition.PropertyType.FLOAT)) {
                if ((value instanceof Float)) {
                    FloatPropertyDefinition floatProperty = (FloatPropertyDefinition) property.getPropertyDefinition();
                    Float floatValue = (float) value;
                    float from = floatProperty.getFrom();
                    float to = floatProperty.getTo();
                    if (floatValue >= from && floatValue <= to)
                        property.updateValue(floatValue, currTick);
                }
            } else if (type.equals(AbstractPropertyDefinition.PropertyType.BOOLEAN)) {
                if ((value instanceof Boolean))
                    property.updateValue(value, currTick);
            } else if (type.equals(AbstractPropertyDefinition.PropertyType.STRING)) {
                if ((value instanceof String))
                    property.updateValue(value, currTick);
            } else {
                throw new InvalidVariableTypeException("performing Set action", type.toString(), value.getClass().getTypeName());
            }
        }
        catch (NumberFormatException e) {
            throw new Exception("Invalid expression, expected " + propertyDefinition.getType().toString());
        }
    }
    @Override
    public List<String> getArguments() {
        List<String> args =  new ArrayList<>();
        args.add(expression.toString());
        return args;
    }
}
