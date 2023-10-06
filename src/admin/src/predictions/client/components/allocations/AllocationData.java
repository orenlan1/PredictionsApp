package predictions.client.components.allocations;

import javafx.beans.property.*;
import javafx.scene.layout.HBox;

public class AllocationData {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty simulationName = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final IntegerProperty requestedAmount = new SimpleIntegerProperty();
    private final StringProperty termination = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final IntegerProperty currentlyRunning = new SimpleIntegerProperty();
    private final IntegerProperty finishedRunning = new SimpleIntegerProperty();
    private final ObjectProperty<HBox> handleButtons = new SimpleObjectProperty<>();

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getSimulationName() {
        return simulationName.get();
    }

    public StringProperty simulationNameProperty() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName.set(simulationName);
    }

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public int getRequestedAmount() {
        return requestedAmount.get();
    }

    public IntegerProperty requestedAmountProperty() {
        return requestedAmount;
    }

    public void setRequestedAmount(int requestedAmount) {
        this.requestedAmount.set(requestedAmount);
    }

    public String getTermination() {
        return termination.get();
    }

    public StringProperty terminationProperty() {
        return termination;
    }

    public void setTermination(String termination) {
        this.termination.set(termination);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public int getCurrentlyRunning() {
        return currentlyRunning.get();
    }

    public IntegerProperty currentlyRunningProperty() {
        return currentlyRunning;
    }

    public void setCurrentlyRunning(int currentlyRunning) {
        this.currentlyRunning.set(currentlyRunning);
    }

    public int getFinishedRunning() {
        return finishedRunning.get();
    }

    public IntegerProperty finishedRunningProperty() {
        return finishedRunning;
    }

    public void setFinishedRunning(int finishedRunning) {
        this.finishedRunning.set(finishedRunning);
    }

    public HBox getHandleButtons() {
        return handleButtons.get();
    }

    public ObjectProperty<HBox> handleButtonsProperty() {
        return handleButtons;
    }

    public void setHandleButtons(HBox handleButtons) {
        this.handleButtons.set(handleButtons);
    }
}
