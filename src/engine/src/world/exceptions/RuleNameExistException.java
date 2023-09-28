package world.exceptions;

public class RuleNameExistException extends Exception{
    private final String ruleName;

    private final String EXCEPTION_MESSAGE = "The rule \"%s\" appear more than once in file.\nEach rule should have different name.\n";

    public RuleNameExistException(String ruleName)
    {
        this.ruleName = ruleName;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, ruleName);
    }
}
