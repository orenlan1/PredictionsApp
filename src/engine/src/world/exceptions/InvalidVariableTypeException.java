package world.exceptions;

public class InvalidVariableTypeException extends Exception {
    private final String location;
    private final String actualType;
    private final String expectedType;
    private final String EXCEPTION_MESSAGE = "Invalid type while %s - expected type of %s and received %s";

    public InvalidVariableTypeException(String location, String expectedType, String actualType) {
        this.location = location;
        this.actualType = actualType;
        this.expectedType = expectedType;
    }

    @Override
    public String getMessage() { return String.format(EXCEPTION_MESSAGE, location, expectedType, actualType); }
}
