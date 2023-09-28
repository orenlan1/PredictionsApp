package world.environment.api;

import world.property.api.PropertyInstance;

import java.util.Collection;
import java.util.Optional;

public interface ActiveEnvironment {
    Optional<PropertyInstance> getProperty(String name);
    Collection<PropertyInstance> getEnvironmentVariables();
    void addPropertyInstance(PropertyInstance propertyInstance);
}
