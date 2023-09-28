package world.context;

import world.action.impl.SecondaryEntity;
import world.entity.api.EntityDefinition;


public interface Context {
    EntityDefinition getPrimaryEntity();
    EntityDefinition getSecondaryEntity();

}
