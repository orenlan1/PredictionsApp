package world.property.impl;

import world.property.api.AbstractPropertyDefinition;
import world.property.api.PropertyDefinition;
import world.property.api.PropertyInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyInstanceImpl implements PropertyInstance, Serializable {
    private final PropertyDefinition propertyDefinition;
    protected Object value;
    protected int lastUpdateTick;
    List<Integer> updateTicksList;

    public PropertyInstanceImpl(PropertyDefinition propertyDefinition) { // Ctor for entity property
        this.propertyDefinition = propertyDefinition;
        this.value = propertyDefinition.generateValue();
        lastUpdateTick = 0;
        updateTicksList = new ArrayList<>();
    }

    public PropertyInstanceImpl(PropertyDefinition propertyDefinition, Object value) { // Ctor for environment property
        this.propertyDefinition = propertyDefinition;
        this.value = value;
        lastUpdateTick = 0;
        updateTicksList = new ArrayList<>();
    }

    @Override
    public PropertyDefinition getPropertyDefinition() {
        return propertyDefinition;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void updateValue(Object value, int currTick)
    {
        if (propertyDefinition.getType().equals(AbstractPropertyDefinition.PropertyType.DECIMAL)) {
            if (value instanceof Integer) {
                IntegerPropertyDefinition intPropertyDef = (IntegerPropertyDefinition) propertyDefinition;
                int to = intPropertyDef.getTo();
                int from = intPropertyDef.getFrom();
                if ((Integer) value > to || (Integer) value < from) {
                    return;
                }
            }
        }
        else if (propertyDefinition.getType().equals(AbstractPropertyDefinition.PropertyType.FLOAT)) {
            if (value instanceof Float) {
                FloatPropertyDefinition floatPropertyDef = (FloatPropertyDefinition) propertyDefinition;
                float to = floatPropertyDef.getTo();
                float from = floatPropertyDef.getFrom();
                if ((float) value > to || (float) value < from) {
                    return;
                }
            }
        }
        if (value instanceof String) {
            if (!((this.value).equals(value))) {
                this.value = value;
                updateTicksList.add(currTick - lastUpdateTick);
                lastUpdateTick = currTick;
            }
        } else if (this.value != value) {
            this.value = value;
            updateTicksList.add((currTick - lastUpdateTick));
            lastUpdateTick = currTick;
        }
    }

    @Override
    public void addLastTick(Integer currTick) {
        updateTicksList.add(currTick - lastUpdateTick);
    }

    @Override
    public Double getAvgUpdateTicks() {
        if (updateTicksList.size() != 0)
            return (updateTicksList.stream()
                    .mapToInt(Integer::intValue)
                    .sum()) / (double) updateTicksList.size();
        else
            return 0d;
    }

    @Override
    public int getLastUpdateTick() {
        return lastUpdateTick;
    }
}
