package dto;

public class TerminationDTO {
    private final Integer ticksCount;
    private final Integer secondCount;

    public TerminationDTO(Integer ticksCount, Integer secondCount) {
        this.ticksCount = ticksCount;
        this.secondCount = secondCount;
    }

    public Integer getTicksCount() {
        return ticksCount;
    }

    public Integer getSecondCount() {
        return secondCount;
    }
}
