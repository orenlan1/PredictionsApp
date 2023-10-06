package dto;

public class AllocationDTO {
    private String simulationName;
    private int executionsFinishedCount;
    private int executionsRunningCount;
    private int id;
    private String status;
    private String username;
    private TerminationDTO terminationDTO;
    private int executionsRequestedCount;

    public AllocationDTO(String simulationName, int executionsFinishedCount, int executionsRunningCount, int id, String status,
                         String username, TerminationDTO terminationDTO, int executionsRequestedCount) {
        this.simulationName = simulationName;
        this.executionsFinishedCount = executionsFinishedCount;
        this.executionsRunningCount = executionsRunningCount;
        this.id = id;
        this.status = status;
        this.username = username;
        this.terminationDTO = terminationDTO;
        this.executionsRequestedCount = executionsRequestedCount;
    }

    public String getUsername() {
        return username;
    }

    public TerminationDTO getTerminationDTO() {
        return terminationDTO;
    }

    public int getExecutionsRequestedCount() {
        return executionsRequestedCount;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public int getExecutionsFinishedCount() {
        return executionsFinishedCount;
    }

    public int getExecutionsRunningCount() {
        return executionsRunningCount;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
