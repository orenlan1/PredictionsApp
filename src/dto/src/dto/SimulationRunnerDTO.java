package dto;

public class SimulationRunnerDTO {
    private final boolean valid;
    private final String message;
    private final int id;
    private final boolean ticksEnded;

    public SimulationRunnerDTO(boolean valid, String message, int id, boolean ticksEnded) {
        this.valid = valid;
        this.message = message;
        this.id = id;
        this.ticksEnded = ticksEnded;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public boolean getTicksEnded() {
        return ticksEnded;
    }
}
