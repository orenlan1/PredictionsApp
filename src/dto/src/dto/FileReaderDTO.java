package dto;

public class FileReaderDTO {
    private final boolean validFile;
    private final String error;
    private final GridDTO gridDTO;

    public FileReaderDTO(boolean valid, String msg, GridDTO gridDTO) {
        validFile = valid;
        error = msg;
        this.gridDTO = gridDTO;
    }

    public boolean isValid() { return validFile; }

    public String getError() { return error; }

    public GridDTO getGridDTO() {
        return gridDTO;
    }
}
