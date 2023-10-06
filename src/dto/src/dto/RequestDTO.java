package dto;

public class RequestDTO {
    private final String simulationName;
    private final int executionsRequestAmount;
    private final TerminationDTO terminationDTO;

    public RequestDTO(String simulationName, int executionsRequestAmount, TerminationDTO terminationDTO) {
        this.simulationName = simulationName;
        this.executionsRequestAmount = executionsRequestAmount;
        this.terminationDTO = terminationDTO;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public int getExecutionsRequestAmount() {
        return executionsRequestAmount;
    }

    public TerminationDTO getTerminationDTO() {
        return terminationDTO;
    }
}
