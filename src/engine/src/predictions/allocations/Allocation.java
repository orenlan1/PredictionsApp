package predictions.allocations;

import world.termination.Termination;

public class Allocation {
    private String simulationName;
    private String username;
    private int executionsLeftCount;
    private int executionsRequestedCount;
    private int executionsFinishedCount = 0;
    private int executionsRunningCount = 0;
    private Termination termination;
    private int id;
    private String status = "unhandled";
    private boolean updated = false;

    public Allocation(String simulationName, String username, int executionsRequestAmount, Termination termination, int id) {
        this.simulationName = simulationName;
        this.username = username;
        this.executionsLeftCount = executionsRequestAmount;
        this.executionsRequestedCount = executionsRequestAmount;
        this.termination = termination;
        this.id = id;
    }

    public int getExecutionsRequestedCount() {
        return executionsRequestedCount;
    }

    public boolean isUpdated() {
        return updated;
    }


    public void setUpdatedToFalse() {
        this.updated = false;
    }

    public void setUpdatedToTrue() {
        this.updated = true;
    }

    public void setExecutionsLeftAmount(int executionsLeftAmount) {
        this.executionsLeftCount = executionsLeftAmount;
    }

    public void setExecutionsFinishedAmount(int executionsFinishedAmount) {
        this.executionsFinishedCount = executionsFinishedAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getUsername() {
        return username;
    }

    public int getExecutionsLeftAmount() {
        return executionsLeftCount;
    }

    public int getExecutionsRunningCount() {
        return executionsRunningCount;
    }

    public int getExecutionsFinishedAmount() {
        return executionsFinishedCount;
    }

    public Termination getTermination() {
        return termination;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public boolean isExecutionLeft() {
        return executionsLeftCount != 0;
    }

    public void executeSimulation() {
        executionsLeftCount--;
    }

    public void increaseCurrentlyRunning() {
        executionsRunningCount++;
    }

    public void decreaseCurrentlyRunning() {
        executionsRunningCount--;
    }
}
