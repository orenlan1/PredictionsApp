package world.rule.activation;

public interface Activation {
    boolean isActive(int tickNumber);
    public Double getProbability();
    public Integer getTicks();
}
