package dto;

public class EntityInitializationDTO {
    private final String name;
    private final Integer population;

    public EntityInitializationDTO(String name, Integer population) {
        this.name = name;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public Integer getPopulation() {
        return population;
    }
}
