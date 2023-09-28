package world.expressions.impl;

import world.entity.api.EntityInstance;
import world.expressions.api.Expression;
import world.expressions.impl.ExpressionImpl;
import world.property.api.AbstractPropertyDefinition;

public class ValueExpression extends ExpressionImpl {

    public ValueExpression(String expression, String type) {
        super(expression, type);
    }

    @Override
    public Object evaluate(EntityInstance entityInstance) {
        switch (type) {
            case "decimal":
                return Integer.parseInt(expression);
            case "float":
                return Float.parseFloat(expression);
            case "boolean":
                return Boolean.parseBoolean(expression);
            default:
                return expression;
        }
    }
}
