package world.entity.impl;

import world.entity.api.EntityDefinition;
import world.entity.api.EntityInstance;
import world.exceptions.EntityPropertyNameExistException;
import world.exceptions.EntityPropertyNotExistException;
import world.grid.Grid;
import world.grid.GridCoordinate;
import world.property.api.PropertyDefinition;
import world.property.impl.PropertyInstanceImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EntityDefinitionImpl implements EntityDefinition, Serializable {
    private final String name;
    private int population;
    private List<EntityInstance> entityInstances;
    private final List<PropertyDefinition> propertiesList;


    public EntityDefinitionImpl(String name) {
        this.name = name;
        this.entityInstances = new LinkedList<>();
        this.propertiesList = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPopulation(Integer population) {
        this.population = population;
    }

    @Override
    public int getPopulation() {
        return population;
    }

    @Override
    public void killInstance() { population--; }

    @Override
    public List<PropertyDefinition> getPropertiesList() {
        return propertiesList;
    }

    @Override
    public void addEntityInstance(EntityInstance entityInstance) {
        this.entityInstances.add(entityInstance);
    }

    @Override
    public void addPropertyDefinition(PropertyDefinition propertyDefinition) throws EntityPropertyNameExistException {
        String propertyName = propertyDefinition.getName();
        for ( PropertyDefinition existingPropertyDefinition : propertiesList)
        {
            if (existingPropertyDefinition.getName().equals(propertyName))
            {
                throw new EntityPropertyNameExistException(name, propertyName);
            }
        }
        this.propertiesList.add(propertyDefinition);
    }

    @Override
    public EntityInstance createEntityInstance(EntityDefinition entityDefinition, Grid grid) {
        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition);
        for (PropertyDefinition propertyDefinition : entityDefinition.getPropertiesList()) {
            Object value = propertyDefinition.generateValue();
            newEntityInstance.addPropertyInstance(new PropertyInstanceImpl(propertyDefinition,value));
        }
        newEntityInstance.setCoordinate(grid);
        return newEntityInstance;
    }

    public EntityInstance createEntityInstance(EntityDefinition entityDefinition, GridCoordinate coordinate) {
        EntityInstance newEntityInstance = new EntityInstanceImpl(entityDefinition);
        for (PropertyDefinition propertyDefinition : entityDefinition.getPropertiesList()) {
            Object value = propertyDefinition.generateValue();
            newEntityInstance.addPropertyInstance(new PropertyInstanceImpl(propertyDefinition,value));
        }
        newEntityInstance.setCoordinate(coordinate);
        return newEntityInstance;
    }

    @Override
    public void createEntityInstancesPopulation(Grid grid) {
        entityInstances = new LinkedList<>();
        for ( int i = 0; i < population; i++) {
            entityInstances.add(createEntityInstance(this, grid));
        }
    }

    @Override
    public List<EntityInstance> getEntityInstances() {
        return entityInstances;
    }

    @Override
    public void removeEntity(EntityInstance entityInstance) {
        this.entityInstances.remove(entityInstance);
    }

    @Override
    public PropertyDefinition getPropertyByName(String propertyName) throws EntityPropertyNotExistException {
        for ( PropertyDefinition propertyDefinition : propertiesList) {
            if (propertyDefinition.getName().equals(propertyName)) {
                return propertyDefinition;
            }
        }
        throw new EntityPropertyNotExistException(this.name, propertyName);
    }

    @Override
    public EntityDefinition cloneEntityDefinition() {
        EntityDefinition newEntity = new EntityDefinitionImpl(this.name);
        newEntity.setPopulation(this.population);
        List<PropertyDefinition> newPropertyDefinitions = new ArrayList<>();

        for (EntityInstance entityInstance : this.getEntityInstances())
            newEntity.addEntityInstance(entityInstance);

        try {
            for (PropertyDefinition propertyDefinition : this.getPropertiesList())
                newEntity.addPropertyDefinition(propertyDefinition);
        } catch (Exception ignored) {}

        return newEntity;
    }
}
