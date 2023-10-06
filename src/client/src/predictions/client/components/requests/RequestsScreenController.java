package predictions.client.components.requests;

import dto.AllocationDTO;
import dto.RequestDTO;
import dto.TerminationDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import predictions.client.components.details.SimulationListRefresher;
import predictions.client.util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static predictions.client.util.Constants.GSON_INSTANCE;
import static predictions.client.util.Constants.PREFIX_BASE_URL;

public class RequestsScreenController {

    @FXML
    private BorderPane requestScreen;

    @FXML
    private TableColumn<RequestData, Integer> currentlyRunningColumn;

    @FXML
    private TableColumn<RequestData, Integer> finishedRunningColumn;

    @FXML
    private TableColumn<RequestData, Integer> idColumn;

    @FXML
    private TableColumn<RequestData, String> nameColumn;

    @FXML
    private TableColumn<RequestData, String> statusColumn;

    @FXML
    private TableColumn<RequestData, Integer> requestedAmountColumn;

    @FXML
    private TableView<RequestData> requestsTable;

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

    private ObservableList<RequestData> tableData = FXCollections.observableArrayList();
    private TimerTask requestsListRefresher;
    private Timer timer;



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


        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().simulationNameProperty());
        requestedAmountColumn.setCellValueFactory(cellData -> cellData.getValue().requestedAmountProperty().asObject());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        currentlyRunningColumn.setCellValueFactory(cellData -> cellData.getValue().currentlyRunningProperty().asObject());
        finishedRunningColumn.setCellValueFactory(cellData -> cellData.getValue().finishedRunningProperty().asObject());
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
        String simulationName = simulationNameField.getText();
        Integer desiredAmount = Integer.parseInt(simulationNumField.getText());
        Integer seconds = (secondsCheckbox.isSelected() && !secondsField.getText().isEmpty()) ? Integer.parseInt(secondsField.getText()) : null;
        Integer ticks = (ticksCheckbox.isSelected() && !ticksField.getText().isEmpty()) ? Integer.parseInt(ticksField.getText()) : null;
        boolean byUser = seconds == null && ticks == null;

        RequestDTO dto = new RequestDTO(simulationName, desiredAmount, new TerminationDTO(ticks, seconds, byUser));
        String dtoAsJson = GSON_INSTANCE.toJson(dto);

        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), dtoAsJson);
        String finalUrl = PREFIX_BASE_URL + "/client/request";

        HttpClientUtil.runAsyncPost(finalUrl, body, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showAlert(Alert.AlertType.ERROR, e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() ->  {
                    try {
                        String responseBody = response.body().string();
                        if (response.isSuccessful()) {
                            showAlert(Alert.AlertType.INFORMATION, "Request sent successfully!");

                            Integer allocationId = GSON_INSTANCE.fromJson(responseBody, Integer.class);
                            tableData.add(new RequestData(simulationName, desiredAmount, allocationId));
                        } else {
                            showAlert(Alert.AlertType.ERROR, responseBody);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

    }


    public RequestData getRequestByUniqueId( TableColumn<RequestData, Integer> idColumn, int targetId) {
        ObservableList<RequestData> tableData = requestsTable.getItems();

        for (RequestData request : tableData) {
            int requestId = idColumn.getCellData(request);
            if (requestId == targetId) {
                return request;
            }
        }
        return null; // Item not found
    }

    public void updateRequestsTable(List<AllocationDTO> allocationDTOList) {
        Platform.runLater(() -> {
            for (AllocationDTO allocation : allocationDTOList) {
                RequestData request = getRequestByUniqueId(idColumn, allocation.getId());
                request.setStatus(allocation.getStatus());
                request.setCurrentlyRunning(allocation.getExecutionsRunningCount());
                request.setFinishedRunning(allocation.getExecutionsFinishedCount());
            }
        });
    }


    public void startRequestsListRefresher() {
        requestsListRefresher = new RequestsListRefresher(this::updateRequestsTable);
        timer = new Timer();
        timer.schedule(requestsListRefresher, 500, 2000);
    }

    public void closeRequestsListRefresher() {
        if (requestsListRefresher != null && timer != null) {
            requestsListRefresher.cancel();
            timer.cancel();
        }
    }

    public void showAlert(Alert.AlertType type, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type, message);
            alert.setHeaderText(null);
            alert.show();
        });
    }
}
