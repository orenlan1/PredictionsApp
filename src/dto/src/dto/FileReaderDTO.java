package dto;

public class FileReaderDTO {
    private final boolean validFile;
    private final String error;
    private final String simulationName;
    private final GridDTO gridDTO;

    public FileReaderDTO(boolean valid, String msg, GridDTO gridDTO, String simulationName) {
        validFile = valid;
        error = msg;
        this.gridDTO = gridDTO;
        this.simulationName = simulationName;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public boolean isValid() { return validFile; }

    public String getError() { return error; }

    public GridDTO getGridDTO() {
        return gridDTO;
    }
}
