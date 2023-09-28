package world.entity.api;

import world.exceptions.EntityPropertyNameExistException;
import world.exceptions.EntityPropertyNotExistException;
import world.grid.Grid;
import world.grid.GridCoordinate;
import world.property.api.PropertyDefinition;

import java.io.Serializable;
import java.util.List;

public interface EntityDefinition  {
    String getName();
    int getPopulation();
    void setPopulation(Integer population);
    void killInstance();
    List<PropertyDefinition> getPropertiesList();
    void addEntityInstance(EntityInstance entityInstance);
    void addPropertyDefinition(PropertyDefinition propertyDefinition) throws EntityPropertyNameExistException;
    PropertyDefinition getPropertyByName(String propertyName) throws EntityPropertyNotExistException;
    EntityInstance createEntityInstance(EntityDefinition entityDefinition, Grid grid);
    EntityInstance createEntityInstance(EntityDefinition entityDefinition, GridCoordinate coordinate);
    void createEntityInstancesPopulation(Grid grid);
    List<EntityInstance> getEntityInstances();
    void removeEntity(EntityInstance entityInstance);
    EntityDefinition cloneEntityDefinition();

}
