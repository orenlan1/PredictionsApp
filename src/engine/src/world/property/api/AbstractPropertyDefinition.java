package world.property.api;

import world.property.api.PropertyDefinition;
import world.property.impl.PropertyInstanceImpl;
import world.value.generator.api.ValueGenerator;

import java.io.Serializable;

public abstract class AbstractPropertyDefinition<T> implements PropertyDefinition, Serializable {
    public enum PropertyType {
        DECIMAL, FLOAT, BOOLEAN, STRING;
    }
    private final String name;
    private final PropertyType type;
    private final ValueGenerator<T> valueGenerator;
    private final Boolean randomInitialize;

    public AbstractPropertyDefinition(String name, PropertyType propertyType, ValueGenerator<T> valueGenerator, Boolean randomInitialize) {
        this.name = name;
        this.type = propertyType;
        this.valueGenerator = valueGenerator;
        this.randomInitialize = randomInitialize;
    }

    @Override
    public Boolean getRandomInitialize() {
        return randomInitialize;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PropertyType getType() {
        return type;
    }

    @Override
    public T generateValue() {
        return valueGenerator.generateValue();
    }

    @Override
    public PropertyInstance createPropertyInstance(PropertyDefinition propertyDefinition) {
        return new PropertyInstanceImpl(this);
    }

}
