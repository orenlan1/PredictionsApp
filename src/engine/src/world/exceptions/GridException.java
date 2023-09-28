package world.exceptions;

public class GridException extends Exception {

    private final String number;
    private final String colsOrRows;;



    private final String EXCEPTION_MESSAGE = "Grid %s got a value of %s.\nGrid columns or rows value must be between 10 and 100.";


    public GridException(String number, String cosOrRows)
    {
        this.number = number;
        this.colsOrRows = cosOrRows;
    }

    @Override
    public String getMessage() {
        return String.format(EXCEPTION_MESSAGE, colsOrRows, number);
    }
}
