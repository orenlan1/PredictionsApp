package dto;

import java.util.List;

public class PastEntityDTO extends EntityDTO {
    private final Integer finalPopulation;

    public PastEntityDTO(String entityName, Integer population, Integer finalPopulation, List<PropertyDTO> propertiesList) {
        super(entityName, population, propertiesList);

        this.finalPopulation = finalPopulation;
    }

    public Integer getFinalPopulation() {
        return finalPopulation;
    }
}
