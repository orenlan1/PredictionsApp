package world.expressions.impl;

import world.action.api.ActionType;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.environment.api.ActiveEnvironment;
import world.expressions.api.Expression;
import world.expressions.impl.ExpressionImpl;
import world.helper.function.api.HelperFunction;
import world.property.api.PropertyDefinition;

public class HelperFunctionExpression extends ExpressionImpl {
    private final HelperFunction helperFunction;

    public HelperFunctionExpression(String expression, String type, HelperFunction helperFunction) {
        super(expression, type);
        this.helperFunction = helperFunction;
    }

    @Override
    public Object evaluate(EntityInstance entityInstance) throws Exception {
        return null;
    }

    public Object evaluate(EntityInstance entityInstance, Integer currTick) throws Exception {
        String arg = expression.split("\\(")[1].split("\\)")[0];
        return helperFunction.invoke(entityInstance, currTick);
    }

    public HelperFunction getHelperFunction() {
        return helperFunction;
    }
}
