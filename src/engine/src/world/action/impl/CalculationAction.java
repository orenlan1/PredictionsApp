package world.action.impl;

import world.action.api.ActionType;
import world.context.Context;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.expressions.api.Expression;
import world.property.api.PropertyDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class CalculationAction extends ActionImpl {
    protected final Expression arg1, arg2;

    protected CalculationAction(ActionType actionType, EntityDefinition entityDefinition, PropertyDefinition propertyDefinition, Expression arg1, Expression arg2, SecondaryEntity secondaryEntity, Context entitiesContext) {
        super(actionType, entityDefinition, propertyDefinition,secondaryEntity, entitiesContext);
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public List<String> getArguments() {
        List<String> args = new ArrayList<>();
        args.add(arg1.toString());
        args.add(arg2.toString());
        return args;
    }
}
