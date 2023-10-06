package dto;

public class TerminationDTO {
    private final Integer ticksCount;
    private final Integer secondCount;
    private final Boolean byUser;

    public TerminationDTO(Integer ticksCount, Integer secondCount, boolean byUser) {
        this.ticksCount = ticksCount;
        this.secondCount = secondCount;
        this.byUser = byUser;
    }

    public Boolean getByUser() {
        return byUser;
    }

    public Integer getTicksCount() {
        return ticksCount;
    }

    public Integer getSecondCount() {
        return secondCount;
    }
}
