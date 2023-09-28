package world.exceptions;

public class EnvironmentVariableNotExistException extends Exception{
    private String envPropertyName;
    private final String EXCEPTION_MESSAGE = "There is no environment variable called \"%s\".\n";

    public EnvironmentVariableNotExistException(String propertyName)
    {
        this.envPropertyName = propertyName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, envPropertyName);
    }
}
