package world.exceptions;

public class EntityPropertyNotExistException extends Exception {
    private String entityName;
    private String propertyName;
    private final String EXCEPTION_MESSAGE = "The entity \"%s\" doesnt have a property called \"%s\".\n";

    public EntityPropertyNotExistException(String entityName, String propertyName)
    {
        this.entityName = entityName;
        this.propertyName = propertyName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, entityName, propertyName);
    }
}
