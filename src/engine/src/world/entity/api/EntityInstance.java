package world.entity.api;

import world.action.api.Action;
import world.grid.Grid;
import world.grid.GridCoordinate;
import world.property.api.PropertyInstance;

import java.util.Collection;


public interface EntityInstance {
    EntityDefinition getEntityDefinition();
    PropertyInstance getPropertyByName(String name);
    void addPropertyInstance(PropertyInstance propertyInstance);
    boolean isAlive();
    void kill();
    EntityInstance createDerivedEntityInstance(EntityDefinition createdEntityDefinition);
    void setCoordinate(Grid grid);
    void setCoordinate(GridCoordinate coordinate);
    GridCoordinate getCoordinate();
    void moveEntityCoordinate(Grid grid);
    boolean isToReplace();
    void setToReplace(boolean toReplace);
    Action getReplaceAction();
    void setReplaceAction(Action action);
    Collection<PropertyInstance> getPropertyInstances();
}
