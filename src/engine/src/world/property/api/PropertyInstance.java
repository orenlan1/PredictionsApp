package world.property.api;

public interface PropertyInstance {
    PropertyDefinition getPropertyDefinition();
    Object getValue();
    void updateValue(Object value, int currTick);
    int getLastUpdateTick();
    void addLastTick(Integer currTick);
    Double getAvgUpdateTicks();
}
