package predictions.client.components.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ClientMainController {

    @FXML
    private Button executionsButton;

    @FXML
    private BorderPane mainClientBorderPane;

    @FXML
    private Button requestsButton;

    @FXML
    private Button resultsButtton;

    @FXML
    private Button simulationsDetailsButton;

    private Stage primaryStage;

    @FXML
    void viewExecutions(ActionEvent event) {

    }

    @FXML
    void viewRequests(ActionEvent event) {

    }

    @FXML
    void viewResults(ActionEvent event) {

    }

    @FXML
    void viewSimulationsDetails(ActionEvent event) {

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

}