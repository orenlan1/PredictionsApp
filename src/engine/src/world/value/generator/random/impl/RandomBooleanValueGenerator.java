package world.value.generator.random.impl;


import world.value.generator.random.api.AbstractRandomValueGenerator;


public class RandomBooleanValueGenerator extends AbstractRandomValueGenerator<Boolean> {

    @Override
    public Boolean generateValue() {
        return random.nextBoolean();
    }
}

