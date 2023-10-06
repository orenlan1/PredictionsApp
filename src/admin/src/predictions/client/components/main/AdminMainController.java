package predictions.client.components.main;
import com.google.gson.reflect.TypeToken;
import dto.FileReaderDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import predictions.client.components.allocations.AllocationsControlller;
import predictions.client.components.management.ManagementController;
import predictions.client.util.http.HttpAdminClientUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
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
    private AllocationsControlller allocationsControlller;


    @FXML
    public void initialize() {
        //initCheckAdminAlive();
        loadManagementPage();
        //initRequestForSimulationsDetails();
        loadAllocationPage();

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
            String managementCss = this.getClass().getResource(MANAGEMENT_CSS_LOCATION).toExternalForm();
            managementPane.getStylesheets().add(managementCss);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllocationPage() {
        URL allocationFXML = getClass().getResource(ALLOCATIONS_FXML_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(allocationFXML);
            fxmlLoader.setResources(this);
            BorderPane allocationsPane = fxmlLoader.load();
            allocationsControlller = fxmlLoader.getController();
            allocationsControlller.setAdminMainController(this);
            String allocationsCss = this.getClass().getResource(ALLOCATIONS_CSS_LOCATION).toExternalForm();
            allocationsPane.getStylesheets().add(allocationsCss);

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
        setMainPanelTo(allocationsControlller.getAllocationScreen());
    }

    @FXML
    void viewExecutionsHistory(ActionEvent event) {

    }

    @FXML
    void viewManagement(ActionEvent event) {
        setMainPanelTo(managementController.getManagementBorderPane());
    }


//    public void initCheckAdminAlive() {
//        String finalUrl = PREFIX_BASE_URL + "/isAlive";
//        HttpAdminClientUtil.runAsync(finalUrl, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Platform.runLater(() -> {
//                    Alert alert = new Alert(Alert.AlertType.ERROR, "HTTP error: " + e.getMessage());
//                    alert.setHeaderText(null);
//                    alert.show();
//                });
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.code() == 400) {
//                    Platform.runLater(() -> {
//                        Alert alert = new Alert(Alert.AlertType.ERROR, "Admin is already logged in." +
//                                "\n Cant set up more than one admin.");
//                        alert.setHeaderText(null);
//                        ButtonType exitButton = new ButtonType("Exit");
//                        alert.getButtonTypes().setAll(exitButton);
//
//                        alert.setOnCloseRequest(event -> {
//                            Platform.exit(); // You can exit the application here
//                        });
//                        alert.showAndWait();
//                    });
//                }
//            }
//        });
//
//    }

    public void initCheckAdminAlive() {
        Response response = null;
        String finalUrl = PREFIX_BASE_URL + "/isAlive";
        try {
            response = HttpAdminClientUtil.runSync(finalUrl);
        } catch (Exception e) {
            Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "HTTP error: " + e.getMessage());
                    alert.setHeaderText(null);
                    alert.show();
                });
        }
        if (response.code() == 400) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Admin is already logged in." +
                                "\n Cant set up more than one admin.");
                        alert.setHeaderText(null);
                        ButtonType exitButton = new ButtonType("Exit");
                        alert.getButtonTypes().setAll(exitButton);

                        alert.setOnCloseRequest(event -> {
                            Platform.exit(); // You can exit the application here
                        });
                        alert.showAndWait();
                    });
                }

    }




    void initRequestForSimulationsDetails()  {
        String finalUrl = PREFIX_BASE_URL + "/allDetails";
        HttpAdminClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "HTTP error: " + e.getMessage());
                    alert.setHeaderText(null);
                    alert.show();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    if ( response.code() == 200) {
                        Type collectionType = new TypeToken<Collection<String>>(){}.getType();
                        Collection<String> allNames = null;
                        try {
                            allNames = GSON_INSTANCE.fromJson(response.body().string(), collectionType);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        managementController.setInitSimulationsNames(allNames);
                    }
                });
            }
        });
    }

}
