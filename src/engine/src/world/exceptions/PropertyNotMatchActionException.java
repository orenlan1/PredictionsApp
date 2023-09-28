package world.exceptions;

public class PropertyNotMatchActionException extends Exception{
    private final String actionName;
    private final String propertyName;
    private final String entityName;
    private final String propertyType;
    private final String EXCEPTION_MESSAGE = "The action \"%s\" should get a number but the property \"%s\" of the entity \"%s\" is %s\n";

    public PropertyNotMatchActionException(String actionName, String propertyName, String entityName, String propertyType) {
        this.actionName = actionName;
        this.propertyName = propertyName;
        this.entityName = entityName;
        this.propertyType = propertyType;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, actionName, propertyName, entityName, propertyType);
    }
}
