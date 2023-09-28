package world.environment.impl;

import world.environment.api.ActiveEnvironment;
import world.environment.api.EnvironmentVariablesManager;
import world.exceptions.EnvironmentVariableNameExistException;
import world.property.api.PropertyDefinition;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentVariablesManagerImpl implements EnvironmentVariablesManager, Serializable {
    Map<String, PropertyDefinition> nameToPropertyDefinition;

    public EnvironmentVariablesManagerImpl() {
        nameToPropertyDefinition = new HashMap<>();
    }

    @Override
    public void addEnvironmentVariable(PropertyDefinition propertyDefinition) throws  EnvironmentVariableNameExistException{
        if ( nameToPropertyDefinition.containsKey(propertyDefinition.getName()))
        {
            throw new EnvironmentVariableNameExistException(propertyDefinition.getName());
        }
        nameToPropertyDefinition.put(propertyDefinition.getName(), propertyDefinition);
    }

    @Override
    public ActiveEnvironment createActiveEnvironment() {
        return new ActiveEnvironmentImpl();
    }

    @Override
    public Collection<PropertyDefinition> getEnvironmentVariables() {
        return nameToPropertyDefinition.values();
    }

    @Override
    public Collection<String> getEnvironmentVariablesNames() {
        return nameToPropertyDefinition.keySet();
    }
}
