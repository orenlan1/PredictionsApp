package world.rule.activation;

import world.World;

import java.io.Serializable;
import java.util.Random;

public class ActivationImpl implements Activation, Serializable {
    private final Double probability;
    private final Integer ticks;

    public ActivationImpl(Double probability, Integer ticks) {
        this.probability = probability;
        this.ticks = ticks;
    }

    @Override
    public Double getProbability() {
        return probability;
    }

    @Override
    public Integer getTicks() {
        return ticks;
    }

    @Override
    public boolean isActive(int tickNumber) {
        Random random = new Random();
        return ((tickNumber % ticks == 0) && (random.nextFloat() < probability));
    }
}
