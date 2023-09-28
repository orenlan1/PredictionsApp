package world.property.impl;

import world.property.api.AbstractPropertyDefinition;
import world.value.generator.api.ValueGenerator;

public class IntegerPropertyDefinition extends AbstractPropertyDefinition<Integer> {
    private final Integer from;
    private final Integer to;

    public IntegerPropertyDefinition(String name, ValueGenerator<Integer> valueGenerator, Integer from, Integer to, Boolean randomInitialize) {
        super(name, PropertyType.DECIMAL, valueGenerator,randomInitialize);
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }
}
