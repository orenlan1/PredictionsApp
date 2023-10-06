package predictions.client.components.main;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import predictions.client.components.details.ClientDetailsController;
import predictions.client.components.login.LoginController;
import predictions.client.components.requests.RequestsScreenController;

import java.io.IOException;
import java.net.URL;

import static predictions.client.util.Constants.*;

public class ClientMainController {

    @FXML
    private BorderPane mainClientBorderPane;

    @FXML
    private Button executionsButton;

    @FXML
    private Button requestsButton;

    @FXML
    private Button resultsButton;

    @FXML
    private Button simulationsDetailsButton;

    @FXML
    private Label nameLabel;

    private Stage primaryStage;
    private LoginController loginController;
    private ClientDetailsController clientDetailsController;
    private RequestsScreenController requestsScreenController;
    private final BooleanProperty disableButtons;

    public ClientMainController() {
        disableButtons = new SimpleBooleanProperty(true);
    }

    @FXML
    public void initialize() {
        loadLoginPage();
        loadDetailsPage();
        loadRequestsPage();
        executionsButton.disableProperty().bind(disableButtons);
        resultsButton.disableProperty().bind(disableButtons);
        requestsButton.disableProperty().bind(disableButtons);
        simulationsDetailsButton.disableProperty().bind(disableButtons);
    }


    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(loginPageUrl);
            GridPane loginPane = fxmlLoader.load();
            loginController = fxmlLoader.getController();
            loginController.setClientMainController(this);
            setMainPanelTo(loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDetailsPage() {
        URL detailsFXML = getClass().getResource(DETAILS_FXML_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(detailsFXML);
            BorderPane managementPane = fxmlLoader.load();
            clientDetailsController = fxmlLoader.getController();
            String detailsCss = this.getClass().getResource(DETAILS_CSS_LOCATION).toExternalForm();
            managementPane.getStylesheets().add(detailsCss);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRequestsPage() {
        URL requestsFXML = getClass().getResource(REQUESTS_FXML_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(requestsFXML);
            BorderPane requestsPane = fxmlLoader.load();
            requestsScreenController = fxmlLoader.getController();
            String requestsCss = this.getClass().getResource(REQUESTS_CSS_LOCATION).toExternalForm();
            requestsPane.getStylesheets().add(requestsCss);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNameLabel(String name) {
        nameLabel.setText("Username: " + name);
    }

    public void clearMainPanel() {
        mainClientBorderPane.setCenter(null);
    }

    private void setMainPanelTo(Parent pane) {
        mainClientBorderPane.setCenter(pane);
    }

    @FXML
    void viewExecutions(ActionEvent event) {
        closeRefreshersAndClear();
    }

    @FXML
    void viewRequests(ActionEvent event) {
        closeRefreshersAndClear();
        requestsScreenController.startRequestsListRefresher();
        setMainPanelTo(requestsScreenController.getRequestScreen());
    }

    @FXML
    void viewResults(ActionEvent event) {
        closeRefreshersAndClear();
    }

    @FXML
    void viewSimulationsDetails(ActionEvent event) {
        closeRefreshersAndClear();
        clientDetailsController.startListRefresher();
        setMainPanelTo(clientDetailsController.getDetailsBorderPane());
    }

    public void closeRefreshersAndClear() {
        clientDetailsController.closeListRefresher();
        requestsScreenController.closeRequestsListRefresher();

        clearMainPanel();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setDisableButtons(boolean disable) { disableButtons.set(disable); }

}