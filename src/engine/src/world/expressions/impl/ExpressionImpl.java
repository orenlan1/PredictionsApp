package world.expressions.impl;

import world.action.api.ActionType;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.environment.api.ActiveEnvironment;
import world.expressions.api.Expression;
import world.helper.function.impl.EnvironmentFunction;
import world.helper.function.impl.RandomFunction;
import world.property.api.AbstractPropertyDefinition;
import world.property.api.PropertyDefinition;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class ExpressionImpl implements Expression, Serializable {
    protected final String expression;
    protected final String type;

    public ExpressionImpl(String expression, String type)
    {
        this.expression = expression;
        this.type = type;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String toString() { return expression; }
}
