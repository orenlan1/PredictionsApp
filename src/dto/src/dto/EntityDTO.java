package dto;

import java.util.List;

public class EntityDTO {
    private final String entityName;
    private final List<PropertyDTO> propertiesList;
    private final int population;

    public EntityDTO(String entityName,int population, List<PropertyDTO> propertiesList) {
        this.entityName = entityName;
        this.propertiesList = propertiesList;
        this.population = population;
    }

    public int getPopulation() {
        return population;
    }

    public String getEntityName() {
        return entityName;
    }


    public List<PropertyDTO> getPropertiesList() {
        return propertiesList;
    }
}
