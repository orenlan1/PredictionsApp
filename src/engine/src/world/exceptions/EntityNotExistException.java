package world.exceptions;

import java.util.function.Supplier;

public class EntityNotExistException extends Exception {
    private String entityName;


    private final String EXCEPTION_MESSAGE = "Entity named \"%s\" does not exist but written in one of the rules actions.\n";

    


    public EntityNotExistException(String entityName)
    {
        this.entityName = entityName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, entityName);
    }
}
