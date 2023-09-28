package dto;

public class EnvVariablesDTO {
    private final String name;
    private final String value;

    public EnvVariablesDTO(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
