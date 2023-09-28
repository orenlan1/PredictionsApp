package dto;

public class PropertyDTO {
    private final String propertyName;
    private final String propertyType;
    private final Double from;
    private final Double to;
    private final Boolean randomInitialize;

    public PropertyDTO(String propertyName, String propertyType, Double from, Double to, Boolean randomInitialize) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.from = from;
        this.to = to;
        this.randomInitialize = randomInitialize;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Boolean getRandomInitialize() {
        return randomInitialize;
    }
}
