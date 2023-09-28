package world.exceptions;

public class InvalidConditionOperatorException extends Exception {
    private final String opertaor;
    private final String EXCEPTION_MESSAGE = "Wrong operator used in Condition action. Operator should be one of \"=\", \"!=\", \"bt\", \"lt\" but received %s instead\n";

    public InvalidConditionOperatorException(String op) {
        opertaor = op;
    }

    @Override
    public String getMessage() { return String.format(EXCEPTION_MESSAGE, opertaor); }
}
