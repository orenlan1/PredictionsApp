package world.property.impl;

import world.property.api.AbstractPropertyDefinition;
import world.value.generator.api.ValueGenerator;

public class StringPropertyDefinition extends AbstractPropertyDefinition<String> {
    public StringPropertyDefinition(String name, ValueGenerator<String> valueGenerator,Boolean randomInitialize) {
        super(name, PropertyType.STRING, valueGenerator,randomInitialize);
    }
}
