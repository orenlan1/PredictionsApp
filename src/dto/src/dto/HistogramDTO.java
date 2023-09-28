package dto;

import java.util.Map;

public class HistogramDTO {
    private final Map<Object, Integer> valueToAmount;

    public HistogramDTO(Map<Object, Integer> valueToAmount) { this.valueToAmount = valueToAmount; }

    public Map<Object, Integer> getValueToAmount() {
        return valueToAmount;
    }
}
