package world.exceptions;

public class SimulationFunctionsException extends Exception{
    private String entityName;
    private String functionName;



    private final String EXCEPTION_MESSAGE = "Error while running the simulation.\n" +
            "Helper function named \"%s\" got an entity named \"%s\" that doesnt exist or is not related to the action.\n";


    public SimulationFunctionsException(String entityName, String functionName)
    {
        this.entityName = entityName;
        this.functionName = functionName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, functionName, entityName);
    }
}
