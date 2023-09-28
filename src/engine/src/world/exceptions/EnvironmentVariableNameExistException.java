package world.exceptions;

public class EnvironmentVariableNameExistException extends Exception{
    private String environmentVariableName;
    private final String EXCEPTION_MESSAGE = "Environment variable \"%s\" already exist. Each environment variable should have a different name.\n";

    public EnvironmentVariableNameExistException(String environmentVariableName)
    {
        this.environmentVariableName = environmentVariableName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, environmentVariableName);
    }
}
