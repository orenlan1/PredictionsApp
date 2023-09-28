package world.value.generator.api;
import world.value.generator.fixed.FixedValueGenerator;
import world.value.generator.random.impl.RandomBooleanValueGenerator;
import world.value.generator.random.impl.RandomFloatValueGenerator;
import world.value.generator.random.impl.RandomIntegerValueGenerator;
import world.value.generator.random.impl.RandomStringValueGenerator;

public interface ValueGeneratorFactory {

    static <T> ValueGenerator<T> createFixed(T value) {
        return new FixedValueGenerator<>(value);
    }

    static ValueGenerator<Boolean> createRandomBoolean() {
        return new RandomBooleanValueGenerator();
    }

    static ValueGenerator<Integer> createRandomInteger(Integer from, Integer to) {
        return new RandomIntegerValueGenerator(from, to);
    }

    static ValueGenerator<Float> createRandomFloat(Float from, Float to) {
        return new RandomFloatValueGenerator(from, to);
    }

    static ValueGenerator<String> createRandomString() {
        return new RandomStringValueGenerator();
    }



}
