package predictions.client.components.allocations;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import predictions.client.components.main.AdminMainController;

import java.net.URL;
import java.util.ResourceBundle;

public class AllocationsController implements Initializable {

    @FXML
    private TableView<?> allocationsTable;

    @FXML
    private TableColumn<?, ?> currentlyRunningColumn;

    @FXML
    private TableColumn<?, ?> finishedRunningColumn;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private BorderPane allocationScreen;

    @FXML
    private TableColumn<?, ?> requestedAmountColumn;

    @FXML
    private TableColumn<?, ?> simulationNameColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> terminationColumn;

    @FXML
    private TableColumn<?, ?> usernameColumn;

    AdminMainController adminMainController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allocationsTable.setPlaceholder(new Label(""));
    }

    public void setAdminMainController(AdminMainController adminMainController) {
        this.adminMainController = adminMainController;
    }

    public BorderPane getAllocationScreen() {
        return allocationScreen;
    }
}