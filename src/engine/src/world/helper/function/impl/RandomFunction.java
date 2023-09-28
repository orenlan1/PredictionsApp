package world.helper.function.impl;

import world.entity.api.EntityInstance;
import world.environment.api.ActiveEnvironment;
import world.value.generator.api.ValueGeneratorFactory;

public class RandomFunction extends HelperFunctionImpl {
    private final String arg;

    public RandomFunction(String arg) {
        super("random", 1, "int");
        this.arg = arg;
    }

    @Override
    public Object invoke(EntityInstance entityInstance, Integer currTick) throws NumberFormatException {
        Integer value = -1;
        try {
            value = Integer.parseInt(arg);
        } catch (NumberFormatException ignored) { }

        return ValueGeneratorFactory.createRandomInteger(0, value).generateValue();
    }
}
