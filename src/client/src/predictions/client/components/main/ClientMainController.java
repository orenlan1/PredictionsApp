package predictions.client.components.main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import predictions.client.components.login.LoginController;

import java.io.IOException;
import java.net.URL;

import static predictions.client.util.Constants.LOGIN_PAGE_FXML_LOCATION;

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

    private LoginController loginController;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();



    @FXML
    public void initialize() {
        loadLoginPage();
    }




    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(loginPageUrl);
            fxmlLoader.setLocation(loginPageUrl);
            AnchorPane loginPane = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setClientMainController(this);
            setMainPanelTo(loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearMainPanel() {
        mainClientBorderPane.setCenter(null);
    }

    private void setMainPanelTo(Parent pane) {
        mainClientBorderPane.setCenter(pane);
    }

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