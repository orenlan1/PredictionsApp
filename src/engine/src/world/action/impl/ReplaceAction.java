package world.action.impl;

import world.action.api.Action;
import world.action.api.ActionType;
import world.context.Context;
import world.context.ContextImpl;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ReplaceAction implements Action, Serializable {
    private ActionType actionType = ActionType.REPLACE;
    private final String mode;
    private EntityDefinition removedEntityDefinition;
    private EntityDefinition createdEntityDefinition;
    private final Context entitiesContext;

    public ReplaceAction(String mode, EntityDefinition removedEntity, EntityDefinition createdEntity, Context entitiesContext) {
        this.mode = mode;
        this.removedEntityDefinition = removedEntity;
        this.createdEntityDefinition = createdEntity;
        this.entitiesContext = entitiesContext;
    }

    @Override
    public void activate(int currTick, EntityInstance... entityInstance) throws Exception {
        if (entityInstance[0].isAlive()) {
            entityInstance[0].kill();
            if (mode.equals("scratch")) {
                EntityInstance newEntity = entityInstance[0].getEntityDefinition().createEntityInstance(createdEntityDefinition, entityInstance[0].getCoordinate());
                createdEntityDefinition.addEntityInstance(newEntity);
            } else if (mode.equals("derived")) {
                EntityInstance newEntity = entityInstance[0].createDerivedEntityInstance(createdEntityDefinition);
                createdEntityDefinition.addEntityInstance(newEntity);
            }
        }
    }

    @Override
    public ActionType getActionType() {
        return actionType;
    }

    @Override
    public EntityDefinition getMainEntityDefinition() {
        return removedEntityDefinition;
    }

    @Override
    public EntityDefinition getSecondaryEntityDefinition() {
        return createdEntityDefinition;
    }

    @Override
    public SecondaryEntity getSecondaryEntityComponent() {
        return null;
    }

    @Override
    public Context getEntitiesContext() {
        return entitiesContext;
    }

    @Override
    public List<String> getArguments() {
        List<String> args = new ArrayList<>();
        args.add(mode);
        return args;
    }

}
