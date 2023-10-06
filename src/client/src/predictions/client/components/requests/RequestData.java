package predictions.client.components.requests;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RequestData {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty simulationName = new SimpleStringProperty();
    private final IntegerProperty requestedAmount = new SimpleIntegerProperty();
    private final StringProperty status = new SimpleStringProperty("unhandled");
    private final IntegerProperty currentlyRunning = new SimpleIntegerProperty(0);
    private final IntegerProperty finishedRunning = new SimpleIntegerProperty(0);

    public RequestData(String simulationName, Integer requestedAmount, Integer id) {
        this.simulationName.set(simulationName);
        this.requestedAmount.set(requestedAmount);
        this.id.set(id);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getSimulationName() {
        return simulationName.get();
    }

    public StringProperty simulationNameProperty() {
        return simulationName;
    }

    public Integer getRequestedAmount() {
        return requestedAmount.get();
    }

    public IntegerProperty requestedAmountProperty() {
        return requestedAmount;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public Integer getCurrentlyRunning() {
        return currentlyRunning.get();
    }

    public IntegerProperty currentlyRunningProperty() {
        return currentlyRunning;
    }

    public Integer getFinishedRunning() {
        return finishedRunning.get();
    }

    public IntegerProperty finishedRunningProperty() {
        return finishedRunning;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setSimulationName(String simulationName) {
        this.simulationName.set(simulationName);
    }

    public void setRequestedAmount(int requestedAmount) {
        this.requestedAmount.set(requestedAmount);
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setCurrentlyRunning(int currentlyRunning) {
        this.currentlyRunning.set(currentlyRunning);
    }

    public void setFinishedRunning(int finishedRunning) {
        this.finishedRunning.set(finishedRunning);
    }
}
