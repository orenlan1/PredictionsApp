package predictions.client.components.main;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import predictions.client.components.management.ManagementController;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;

import static predictions.client.util.Constants.*;

public class AdminMainController extends ResourceBundle {


    @FXML
    private BorderPane mainAdminBorderPane;

    @FXML
    private Button allocationsButton;

    @FXML
    private Button executionsHistoryButton;

    @FXML
    private Button managementButton;

    private Stage primaryStage;


    private ManagementController managementController;


    @FXML
    public void initialize() {
        loadManagementPage();
    }

    @Override
    protected Object handleGetObject(@NotNull String key) {
        switch (key) {
            case "mainBorderPane" :
                return mainAdminBorderPane;
            default:
                return null;

        }
    }


    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(keySet());
    }

    private void loadManagementPage() {
        URL managementFXML = getClass().getResource(MANAGEMENT_FXML_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(managementFXML);
            fxmlLoader.setResources(this);
            BorderPane managementPane = fxmlLoader.load();
            managementController = fxmlLoader.getController();
            managementController.setAdminMainController(this);
            String mainCss = this.getClass().getResource(MANAGEMENT_CSS_LOCATION).toExternalForm();
            managementPane.getStylesheets().add(mainCss);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setMainPanelTo(Parent pane) {
        mainAdminBorderPane.setCenter(pane);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @FXML
    void viewAllocations(ActionEvent event) {

    }

    @FXML
    void viewExecutionsHistory(ActionEvent event) {

    }

    @FXML
    void viewManagement(ActionEvent event) {
        setMainPanelTo(managementController.getManagementBorderPane());
    }

}
