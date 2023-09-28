package world.expressions.impl;

import world.entity.api.EntityInstance;
import world.expressions.api.Expression;
import world.property.api.PropertyDefinition;

public class PropertyNameExpression extends ExpressionImpl {
    private final PropertyDefinition property;

    public PropertyNameExpression(String expression, String type, PropertyDefinition propertyDefinition) {
        super(expression, type);
        property = propertyDefinition;
    }

    @Override
    public Object evaluate(EntityInstance entityInstance) {
        return entityInstance.getPropertyByName(property.getName()).getValue();
    }
}
