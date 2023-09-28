package world.action.impl;

import world.action.api.Action;
import world.action.api.ActionType;
import world.context.Context;
import world.context.ContextImpl;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.expressions.api.Expression;
import world.expressions.impl.HelperFunctionExpression;
import world.grid.Grid;
import world.grid.GridCoordinate;
import world.helper.function.impl.PercentFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProximityAction implements Action, Serializable {

    private ActionType actionType = ActionType.PROXIMITY;
    private Expression of;
    private final EntityDefinition sourceEntity;
    private final EntityDefinition targetEntity;
    private final List<Action> actionList;
    private final Context entitiesContext;

    public ProximityAction(EntityDefinition sourceEntity, EntityDefinition targetEntity, Expression expression, Context entitiesContext) {
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.of = expression;
        actionList = new ArrayList<>();
        this.entitiesContext = entitiesContext;
    }

    @Override
    public void activate(int currTick, EntityInstance... entityInstance) throws Exception {
        List<EntityInstance> targetEntities = targetEntity.getEntityInstances();
        EntityInstance instanceInProximity = null;
        boolean inProximity = false;
        int depthLevel;
        if (of instanceof HelperFunctionExpression) {
            HelperFunctionExpression helperOf = (HelperFunctionExpression) of;
            if (helperOf.getHelperFunction() instanceof PercentFunction) {
                PercentFunction percentFunction = (PercentFunction) helperOf.getHelperFunction();
                if ( entityInstance.length >=2)
                    depthLevel = (int) percentFunction.percentInvoke(entityInstance[0], entityInstance[1], currTick);
                else
                    depthLevel = (int) percentFunction.percentInvoke(entityInstance[0], null, currTick);
            }
            else {
                try {
                    depthLevel = ((Float) helperOf.evaluate(entityInstance[0], currTick)).intValue();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else
            depthLevel = (int) of.evaluate(entityInstance[0]);

        GridCoordinate sourceCoordinate = entityInstance[0].getCoordinate();
        for (EntityInstance targetInstance : targetEntities) {
            GridCoordinate targetCoordinate = targetInstance.getCoordinate();
            if (sourceCoordinate.isCoordinateInProximity(targetCoordinate, depthLevel)) {
                instanceInProximity = targetInstance;
                inProximity = true;
                break;
            }
        }
        if (inProximity) {
            for (Action action : actionList)
                if (action.getActionType().equals(ActionType.REPLACE)) {
                    entityInstance[0].setReplaceAction(action);
                    entityInstance[0].setToReplace(true);
                } else {
                    action.activate(currTick, entityInstance[0], instanceInProximity);
                }
        }
    }

    public void addAction(Action action) {
        actionList.add(action);
    }

    @Override
    public Context getEntitiesContext() {
        return entitiesContext;
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntityDefinition getMainEntityDefinition() {
        return sourceEntity;
    }

    @Override
    public SecondaryEntity getSecondaryEntityComponent() {
        return null;
    }

    @Override
    public EntityDefinition getSecondaryEntityDefinition() {
        return targetEntity;
    }

    @Override
    public List<String> getArguments() {
        List<String> args = new ArrayList<>();
        args.add(of.toString());
        args.add(Integer.toString(actionList.size()));
        return args;
    }
}
