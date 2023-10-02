package predictions.client.components.management;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import predictions.client.components.main.AdminMainController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import predictions.client.components.management.details.entity.EntityCardController;
import predictions.client.components.management.details.environment.variable.EnvVariableCardController;
import predictions.client.components.management.details.grid.GridCardController;
import predictions.client.components.management.details.rules.manager.RulesManagerController;
import predictions.client.util.http.HttpAdminClientUtil;
import static predictions.client.util.Constants.*;


public class ManagementController implements Initializable {


    @FXML
    private BorderPane managementBorderPane;

    @FXML
    private Button clearButton;

    @FXML
    private BorderPane detailsBorderPane;

    @FXML
    private FlowPane detailsFlowPane;

    @FXML
    private ScrollPane detailsScrollPane;

    @FXML
    private Button entitiesButton;

    @FXML
    private Button envVariablesButton;

    @FXML
    private Label filePathLabel;

    @FXML
    private Button gridButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private Button rulesButton;

    @FXML
    private TableColumn<String, String> simulationsNameColumn;

    @FXML
    private TableView<String> simulationsTable;

    @FXML
    private Label threadCountLabel;

    @FXML
    private Label currentlyRunningCounter;

    @FXML
    private Label inQueueCounter;

    @FXML
    private Label finishedCounter;

    @FXML
    private Button threadsCountButton;

    @FXML
    private TextField threadCountInput;

    private ObservableList<String> tableData = FXCollections.observableArrayList();

    private Map<String,String> simNameToFilePath = new HashMap<>();

    private AdminMainController adminMainController;
    private Stage primaryStage;
    private final SimpleStringProperty loadedFilePathProperty;
    private final SimpleBooleanProperty isFileLoaded;
    private SimulationInfoDTO simulationDetails;


    public ManagementController() {
        loadedFilePathProperty = new SimpleStringProperty("");
        isFileLoaded = new SimpleBooleanProperty(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label empty = new Label("");
        simulationsTable.setPlaceholder(empty);
        simulationsNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        simulationsTable.setItems(tableData);
        envVariablesButton.disableProperty().bind(isFileLoaded.not());
        entitiesButton.disableProperty().bind(isFileLoaded.not());
        rulesButton.disableProperty().bind(isFileLoaded.not());
        gridButton.disableProperty().bind(isFileLoaded.not());
        filePathLabel.textProperty().bind(Bindings.concat("File path: ", loadedFilePathProperty));
        threadsCountButton.disableProperty().bind(threadCountInput.textProperty().isEmpty());

        threadCountInput.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    threadCountInput.setText(oldValue);
                }
            }
        });

        simulationsTable.setRowFactory(tv -> {
            TableRow<String> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    String rowData = row.getItem();
                    loadedFilePathProperty.set(simNameToFilePath.get(rowData));

                    HttpUrl.Builder urlBuilder = HttpUrl.parse(PREFIX_BASE_URL + "/details").newBuilder();
                    urlBuilder.addQueryParameter("name", rowData);
                    String finalUrl = urlBuilder.build().toString();
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
                                try {
                                    simulationDetails = GSON_INSTANCE.fromJson(response.body().string(), SimulationInfoDTO.class);
                                    int a = 1;
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    });
                }
            });
            return row;
        });
    }


    @FXML
    void clearDetails(ActionEvent event) {
        
    }

    @FXML
    void loadNewFileAction(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select xml simulation file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files","*.xml"));
        File selectedFile = fileChooser.showOpenDialog(adminMainController.getPrimaryStage());
        if (selectedFile == null) {
            return;
        }

        RequestBody body = new MultipartBody.Builder()
                .addFormDataPart("file", selectedFile.getAbsolutePath(), RequestBody.create(selectedFile,MediaType.parse("text/plain")))
                .build();

        String finalUrl = PREFIX_BASE_URL + "/loadFile";
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        Call call = HttpAdminClientUtil.HTTP_CLIENT.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            String rawBody = responseBody.string();
            FileReaderDTO fileReaderDTO = GSON_INSTANCE.fromJson(rawBody, FileReaderDTO.class);
            boolean isValid = fileReaderDTO.isValid();
            if (isValid) {
                simNameToFilePath.put(fileReaderDTO.getSimulationName(),selectedFile.getAbsolutePath());
                isFileLoaded.set(true);
                tableData.add(fileReaderDTO.getSimulationName());
                loadedFilePathProperty.set(selectedFile.getAbsolutePath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "File loaded successfully.");
                alert.setHeaderText(null);
                alert.show();
            } else {
                String error = fileReaderDTO.getError();
                Alert alert = new Alert(Alert.AlertType.ERROR, error);
                alert.setHeaderText(null);
                alert.show();
            }
        }

//        HttpAdminClientUtil.asyncFileLoad(finalUrl, body, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR,"something went wrong" );
//                alert.setHeaderText(null);
//                alert.show();
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    ResponseBody responseBody = response.body();
//
//                    String rawBody = responseBody.string();
//                    FileReaderDTO fileReaderDTO = GSON_INSTANCE.fromJson(rawBody, FileReaderDTO.class);
//                    boolean isValid = fileReaderDTO.isValid();
//                    if (isValid) {
//                        isFileLoaded.set(true);
//                    } else {
//                        String error = fileReaderDTO.getError();
//                        Alert alert = new Alert(Alert.AlertType.ERROR, error);
//                        alert.setHeaderText(null);
//                        alert.show();
//                    }
//
//                }
//            }
//        });
    }

    @FXML
    void setThreadsCount(ActionEvent event) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("threadCount", threadCountInput.getText());
        String jsonObjectAsString = jsonObject.toString();
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonObjectAsString);

        String finalUrl = PREFIX_BASE_URL + "/threadCount";
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();
        Call call = HttpAdminClientUtil.HTTP_CLIENT.newCall(request);
        Response response = call.execute();

        Alert alert =  new Alert(Alert.AlertType.INFORMATION, "Thread count set successfully!");
        if (response.isSuccessful())
            threadCountLabel.setText(threadCountInput.getText());
        else
            alert =  new Alert(Alert.AlertType.ERROR, "Could not set thread count");

        alert.setHeaderText(null);
        alert.show();
    }

    @FXML
    void showEntities(ActionEvent event) throws Exception {
        detailsFlowPane.getChildren().clear();
        detailsBorderPane.setCenter(detailsScrollPane);

        for (EntityDTO dto: simulationDetails.getEntitiesList()) {
            URL entityDetailsFXML = getClass().getResource("/predictions/client/components/management/details/entity/entityDetails.fxml");
            FXMLLoader loader = new FXMLLoader(entityDetailsFXML);
            GridPane entityCard = loader.load();

            EntityCardController entityCardController = loader.getController();
            entityCardController.setName(dto.getEntityName());
            entityCardController.setProperties(dto.getPropertiesList());
            entityCard.getStylesheets().add("/predictions/client//components/management/details/entity/entity.css");

            detailsFlowPane.getChildren().add(entityCard);
        }
    }

    @FXML
    void showEnvVariables(ActionEvent event) throws Exception {
        detailsFlowPane.getChildren().clear();
        detailsBorderPane.setCenter(detailsScrollPane);

        for (PropertyDTO dto : simulationDetails.getEnvVariablesList()) {
            URL envVariableDetailsFXML = getClass().getResource("/predictions/client/components/management/details/environment/variable/environmentVariableDetails.fxml");
            FXMLLoader loader = new FXMLLoader(envVariableDetailsFXML);
            GridPane envVariableCard = loader.load();

            EnvVariableCardController envVariableCardController = loader.getController();
            envVariableCardController.setEnvVariableCard(dto);
            envVariableCard.getStylesheets().add("/predictions/client/components/management/details/environment/variable/envVariables.css");

            detailsFlowPane.getChildren().add(envVariableCard);
        }
    }

    @FXML
    void showGrid(ActionEvent event) throws Exception{
        detailsFlowPane.getChildren().clear();
        detailsBorderPane.setCenter(detailsScrollPane);
        URL gridDetailsFXML = getClass().getResource("/predictions/client/components/management/details/grid/gridDetails.fxml");
        FXMLLoader loader = new FXMLLoader(gridDetailsFXML);
        GridPane gridCard = loader.load();

        GridCardController gridCardController = loader.getController();
        gridCardController.setAxisLabels(simulationDetails.getGrid().getX(), simulationDetails.getGrid().getY());
        gridCard.getStylesheets().add("/predictions/client/components/management/details/grid/grid.css");

        detailsFlowPane.getChildren().add(gridCard);

    }

    @FXML
    void showRules(ActionEvent event) throws Exception {
        detailsFlowPane.getChildren().clear();

        URL rulesManagerDetailsFXML = getClass().getResource("/predictions/client/components/management/details/rules/manager/rulesManager.fxml");
        FXMLLoader loader = new FXMLLoader(rulesManagerDetailsFXML);
        BorderPane rulesManager = loader.load();

        RulesManagerController rulesManagerController = loader.getController();
        rulesManagerController.showRuleCards(simulationDetails.getRulesList());
        detailsBorderPane.setCenter(rulesManager);
    }


    public void setAdminMainController(AdminMainController adminMainController) {
        this.adminMainController = adminMainController;
    }

    public BorderPane getManagementBorderPane() {
        return managementBorderPane;
    }

}
