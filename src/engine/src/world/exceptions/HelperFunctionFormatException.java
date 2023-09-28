package world.exceptions;

public class HelperFunctionFormatException extends Exception {
    private final String functionName;
    private final String expected;
    private final String received;

    private final String EXCEPTION_MESSAGE = "The function \"%s\" expects to receive \"%s\" format, received \"%s\" instead.";

    public HelperFunctionFormatException(String functionName, String expected, String received) {
        this.functionName = functionName;
        this.expected = expected;
        this.received = received;
    }

    @Override
    public String getMessage() { return String.format(EXCEPTION_MESSAGE, functionName, expected, received); }

}
