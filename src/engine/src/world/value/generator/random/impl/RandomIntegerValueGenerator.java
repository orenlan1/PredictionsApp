package world.value.generator.random.impl;

import world.value.generator.random.api.AbstractRandomValueGenerator;

public class RandomIntegerValueGenerator extends AbstractRandomValueGenerator<Integer> {
    private final Integer from;
    private final Integer to;

    public RandomIntegerValueGenerator(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }


    @Override
    public Integer generateValue() {
        return random.nextInt(to - from) + from;
    }
}
