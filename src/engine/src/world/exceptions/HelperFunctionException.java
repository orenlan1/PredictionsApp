package world.exceptions;

import world.helper.function.impl.HelperFunctionImpl;

public class HelperFunctionException extends Exception {

    private final String entityName;
    private final String functionName;
    private final String actionName;



    private final String EXCEPTION_MESSAGE = "The helper function named \"%s\" related to the action \"%s\"\n" +
            "got an entity named \"%s\" that doesn't exist or is not related to the action.\n";


    public HelperFunctionException(String entityName, String functionName, String actionName)
    {
        this.entityName = entityName;
        this.functionName = functionName;
        this.actionName = actionName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, functionName, actionName, entityName);
    }
}
