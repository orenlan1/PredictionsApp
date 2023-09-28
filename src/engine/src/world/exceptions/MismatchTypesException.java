package world.exceptions;

public class MismatchTypesException extends Exception {
    private final String receiver;
    private final String expected;
    private final String arg;

    private final String EXCEPTION_MESSAGE = "%s received a non-%s variable, received %s instead.\n";

    public MismatchTypesException(String receiver, String expected, String arg) {
        this.receiver = receiver;
        this.expected = expected;
        this.arg = arg;
    }

    @Override
    public String getMessage() { return String.format(EXCEPTION_MESSAGE, receiver, expected, arg); }
}
