package dto;

import java.util.List;

public class PropertiesDTO {
    private final List<PropertyDTO> DTOs;

    public PropertiesDTO(List<PropertyDTO> DTOs) {
        this.DTOs = DTOs;
    }

    public List<PropertyDTO> getPropertiesDTO() {
        return DTOs;
    }
}