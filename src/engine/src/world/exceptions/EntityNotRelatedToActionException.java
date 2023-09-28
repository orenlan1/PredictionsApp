package world.exceptions;

public class EntityNotRelatedToActionException extends Exception {
    String entityName;
    String actionName;
    String acceptableEntities;
    private final String EXCEPTION_MESSAGE = "The entity \"%s\" does not relate to the action \"%s\".\n" +
            "The action can receive the following entities: %s.";

    public EntityNotRelatedToActionException(String entityName, String actionName, String acceptableEntities)
    {
        this.entityName = entityName;
        this.actionName = actionName;
        this.acceptableEntities = acceptableEntities;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, entityName, actionName, acceptableEntities);
    }

}
