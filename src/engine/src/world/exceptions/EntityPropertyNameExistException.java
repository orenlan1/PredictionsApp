package world.exceptions;

import world.entity.api.EntityDefinition;
import world.property.api.PropertyDefinition;

public class EntityPropertyNameExistException extends Exception {
    private String entityName;
    private String propertyName;
    private final String EXCEPTION_MESSAGE = "The entity \"%s\" already has a property called \"%s\". Each property of entity should have a different name.\n";

    public EntityPropertyNameExistException(String entityName, String propertyName)
    {
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, entityName, propertyName);
    }
}
