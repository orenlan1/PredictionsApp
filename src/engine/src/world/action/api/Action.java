package world.action.api;

import world.action.impl.SecondaryEntity;
import world.context.Context;
import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;

import java.util.List;

public interface Action {
    void activate(int currTick, EntityInstance... entityInstance) throws Exception;
    ActionType getActionType();
    EntityDefinition getMainEntityDefinition();
    EntityDefinition getSecondaryEntityDefinition();
    List<String> getArguments();
    SecondaryEntity getSecondaryEntityComponent();
    Context getEntitiesContext();
}
