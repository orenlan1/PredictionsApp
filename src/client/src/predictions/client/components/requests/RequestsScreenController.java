package predictions.client.components.requests;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class RequestsScreenController {

    @FXML
    private BorderPane requestScreen;

    @FXML
    private TableColumn<?, ?> currentlyRunningColumn;

    @FXML
    private TableColumn<?, ?> finishedRunningColumn;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> requestedAmountColumn;

    @FXML
    private TableView<String> requestsTable;

    @FXML
    private CheckBox secondsCheckbox;

    @FXML
    private CheckBox ticksCheckbox;

    @FXML
    private TextField secondsField;

    @FXML
    private TextField simulationNameField;

    @FXML
    private TextField simulationNumField;

    @FXML
    private TextField ticksField;

    @FXML
    private Button sendButton;

    private ObservableList<String> tableData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        requestsTable.setPlaceholder(new Label(""));
        requestsTable.setItems(tableData);
        secondsField.disableProperty().bind(secondsCheckbox.selectedProperty().not());
        ticksField.disableProperty().bind(ticksCheckbox.selectedProperty().not());
        simulationNumField.textProperty().addListener((obs, oldValue, newValue) -> textFieldValidation(simulationNumField, oldValue, newValue));
        secondsField.textProperty().addListener((obs, oldValue, newValue) -> textFieldValidation(secondsField, oldValue, newValue));
        ticksField.textProperty().addListener((obs, oldValue, newValue) -> textFieldValidation(ticksField, oldValue, newValue));
        sendButton.disableProperty().bind(simulationNameField.textProperty().isEmpty().or(simulationNumField.textProperty().isEmpty()));

    }

    public BorderPane getRequestScreen() {
        return requestScreen;
    }

    public void textFieldValidation(TextField field, String oldValue, String newValue) {
        if (!newValue.isEmpty()) {
            try {
                Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                field.setText(oldValue);
            }
        }
    }

    @FXML
    public void sendRequest(ActionEvent event) {

    }
}
