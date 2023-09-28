package world.environment.api;

import world.exceptions.EnvironmentVariableNameExistException;
import world.property.api.PropertyDefinition;

import java.io.Serializable;
import java.util.Collection;

public interface EnvironmentVariablesManager {
    void addEnvironmentVariable(PropertyDefinition propertyDefinition) throws EnvironmentVariableNameExistException;
    ActiveEnvironment createActiveEnvironment();
    Collection<PropertyDefinition> getEnvironmentVariables();
    Collection<String> getEnvironmentVariablesNames();
}
