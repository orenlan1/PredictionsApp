package world.value.generator.random.impl;

import world.value.generator.random.api.AbstractRandomValueGenerator;

public class RandomFloatValueGenerator extends AbstractRandomValueGenerator<Float> {

    private final Float from;
    private final Float to;

    public RandomFloatValueGenerator(Float from, Float to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Float generateValue() {
        return  from + random.nextFloat() * (to - from);
    }
}
