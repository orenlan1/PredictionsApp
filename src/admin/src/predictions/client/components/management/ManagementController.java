package predictions.client.components.management;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.FileReaderDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import predictions.client.components.main.AdminMainController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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
    private Button gridAndTerminationButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private Button rulesButton;

    @FXML
    private TableColumn<String, String> simulationsNameColumn;

    @FXML
    private TableView<String> simulationsTable;

    @FXML
    private Button threadsCountButton;

    private ObservableList<String> tableData = FXCollections.observableArrayList();

    private Map<String,String> simNameToFilePath = new HashMap<>();

    private AdminMainController adminMainController;
    private Stage primaryStage;
    private final SimpleStringProperty loadedFilePathProperty;
    private final SimpleBooleanProperty isFileLoaded;


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
        gridAndTerminationButton.disableProperty().bind(isFileLoaded.not());
        filePathLabel.textProperty().bind(Bindings.concat("File path: ", loadedFilePathProperty));


        simulationsTable.setRowFactory(tv -> {
            TableRow<String> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    String rowData = row.getItem();
                    loadedFilePathProperty.set(simNameToFilePath.get(rowData));
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

        String finalUrl = PREFIX_BASE_URL + "/predictions/loadFile";
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
    void setThreadsCount(ActionEvent event) {

    }

    @FXML
    void showEntities(ActionEvent event) {

    }

    @FXML
    void showEnvVariables(ActionEvent event) {

    }

    @FXML
    void showGridAndTermination(ActionEvent event) {

    }

    @FXML
    void showRules(ActionEvent event) {

    }


    public void setAdminMainController(AdminMainController adminMainController) {
        this.adminMainController = adminMainController;
    }

    public BorderPane getManagementBorderPane() {
        return managementBorderPane;
    }

}
