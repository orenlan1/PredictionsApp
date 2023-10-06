package predictions.client.components.allocations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import predictions.client.components.main.AdminMainController;

import java.net.URL;
import java.util.ResourceBundle;

public class AllocationsController implements Initializable {

    @FXML
    private TableView<AllocationData> allocationsTable;

    @FXML
    private TableColumn<AllocationData, Integer> currentlyRunningColumn;

    @FXML
    private TableColumn<AllocationData, Integer> finishedRunningColumn;

    @FXML
    private TableColumn<AllocationData, Integer> idColumn;

    @FXML
    private BorderPane allocationScreen;

    @FXML
    private TableColumn<AllocationData, Integer> requestedAmountColumn;

    @FXML
    private TableColumn<AllocationData, String> simulationNameColumn;

    @FXML
    private TableColumn<AllocationData, String> statusColumn;

    @FXML
    private TableColumn<AllocationData, String> terminationColumn;

    @FXML
    private TableColumn<AllocationData, String> usernameColumn;

    @FXML
    private TableColumn<AllocationData, HBox> handleColumn;

    private ObservableList<AllocationData> allocationDataList = FXCollections.observableArrayList();
    AdminMainController adminMainController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allocationsTable.setPlaceholder(new Label(""));

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        simulationNameColumn.setCellValueFactory(cellData -> cellData.getValue().simulationNameProperty());
        usernameColumn.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        requestedAmountColumn.setCellValueFactory(cellData -> cellData.getValue().requestedAmountProperty().asObject());
        terminationColumn.setCellValueFactory(cellData -> cellData.getValue().terminationProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        currentlyRunningColumn.setCellValueFactory(cellData -> cellData.getValue().currentlyRunningProperty().asObject());
        finishedRunningColumn.setCellValueFactory(cellData -> cellData.getValue().finishedRunningProperty().asObject());
        handleColumn.setCellValueFactory(cellData -> cellData.getValue().handleButtonsProperty());

        allocationsTable.setItems(allocationDataList);

        AllocationData allocation1 = new AllocationData();
        allocation1.setId(1);
        allocation1.setSimulationName("Simulation 1");
        allocation1.setUsername("User 1");
        allocation1.setRequestedAmount(10);
        allocation1.setTermination("Termination 1");
        allocation1.setStatus("Running");
        allocation1.setCurrentlyRunning(5);
        allocation1.setFinishedRunning(2);
        allocation1.setHandleButtons(createHandleButtons());

        AllocationData allocation2 = new AllocationData();
        allocation2.setId(2);
        allocation2.setSimulationName("Simulation 2");
        allocation2.setUsername("User 2");
        allocation2.setRequestedAmount(20);
        allocation2.setTermination("Termination 2");
        allocation2.setStatus("Finished");
        allocation2.setCurrentlyRunning(0);
        allocation2.setFinishedRunning(20);
        allocation2.setHandleButtons(createHandleButtons());

        allocationDataList.addAll(allocation1, allocation2);
    }

    private HBox createHandleButtons() {
        // Create buttons and add them to an HBox
        Button acceptButton = new Button("Accept");
        Button rejectButton = new Button("Reject");
        HBox buttonsContainer = new HBox(10);
        buttonsContainer.getChildren().addAll(acceptButton, rejectButton);


        acceptButton.setId("acceptButton");
        rejectButton.setId("rejectButton");
        acceptButton.getStyleClass().add("handleButton");
        rejectButton.getStyleClass().add("handleButton");
        acceptButton.setOnAction(event -> handleAcceptButtonClick());
        rejectButton.setOnAction(event -> handleRejectButtonClick());

        return buttonsContainer;
    }

    private void handleAcceptButtonClick() {
        // Handle the edit button click
    }

    private void handleRejectButtonClick() {
        // Handle the delete button click
    }

    public void setAdminMainController(AdminMainController adminMainController) {
        this.adminMainController = adminMainController;
    }

    public BorderPane getAllocationScreen() {
        return allocationScreen;
    }
}