package world.property.api;


public interface PropertyDefinition {
    String getName();
    AbstractPropertyDefinition.PropertyType getType();
    Boolean getRandomInitialize();
    Object generateValue();
    PropertyInstance createPropertyInstance(PropertyDefinition propertyDefinition);
}
