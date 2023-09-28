package world.property.impl;

import world.property.api.AbstractPropertyDefinition;
import world.value.generator.api.ValueGenerator;

public class BooleanPropertyDefinition extends AbstractPropertyDefinition<Boolean> {
    public BooleanPropertyDefinition(String name, ValueGenerator<Boolean> valueGenerator,Boolean randomInitialize) {
        super(name, PropertyType.BOOLEAN, valueGenerator, randomInitialize);
    }
}
