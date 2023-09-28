package dto;

public class MeanPropertyDTO {
    private final Double mean;
    private final boolean valid;
    private final String message;

    public MeanPropertyDTO(boolean valid, Double mean, String message) {
        this.valid = valid;
        this.mean = mean;
        this.message = message;
    }

    public Double getMean() {
        return mean;
    }

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return valid;
    }
}
