package dto;

public class EnvVariableSetValidationDTO {
    private final Boolean validation;
    private final String message;

    public EnvVariableSetValidationDTO(Boolean validation, String msg) {
        this.validation = validation;
        this.message = msg;
    }

    public Boolean getValidation() {
        return validation;
    }

    public String getMessage() {
        return message;
    }
}
