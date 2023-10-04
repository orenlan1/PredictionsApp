package predictions.client.components.details;

import dto.EntityDTO;
import dto.PropertyDTO;
import dto.SimulationInfoDTO;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import predictions.client.components.details.entity.EntityCardController;
import predictions.client.components.details.environment.variable.EnvVariableCardController;
import predictions.client.components.details.grid.GridCardController;
import predictions.client.components.details.rules.manager.RulesManagerController;
import predictions.client.components.main.ClientMainController;
import predictions.client.util.http.HttpClientUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static predictions.client.util.Constants.GSON_INSTANCE;
import static predictions.client.util.Constants.PREFIX_BASE_URL;


public class ClientDetailsController implements Initializable {

    @FXML
    private Button clearButton;

    @FXML
    private FlowPane detailsFlowPane;

    @FXML
    private ScrollPane detailsScrollPane;

    @FXML
    private Button entitiesButton;

    @FXML
    private Button envVariablesButton;

    @FXML
    private Button gridButton;

    @FXML
    private BorderPane detailsBorderPane;

    @FXML
    private Button rulesButton;

    @FXML
    private TableColumn<String, String> simulationsNameColumn;

    @FXML
    private TableView<String> simulationsTable;

    private ObservableList<String> tableData = FXCollections.observableArrayList();

    private ClientMainController clientMainController;
    private Stage primaryStage;
    private final SimpleBooleanProperty isSimulationSelected;
    private SimulationInfoDTO simulationDetails;
    private TimerTask listRefresher;
    private Timer timer;


    public ClientDetailsController() {
        isSimulationSelected = new SimpleBooleanProperty(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Label empty = new Label("");
        simulationsTable.setPlaceholder(empty);
        simulationsNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        simulationsTable.setItems(tableData);
        envVariablesButton.disableProperty().bind(isSimulationSelected.not());
        entitiesButton.disableProperty().bind(isSimulationSelected.not());
        rulesButton.disableProperty().bind(isSimulationSelected.not());
        gridButton.disableProperty().bind(isSimulationSelected.not());

        simulationsTable.setRowFactory(tv -> {
            TableRow<String> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    detailsBorderPane.setCenter(detailsScrollPane);
                    detailsFlowPane.getChildren().clear();
                    String rowData = row.getItem();
                    sendRequestForSimulationInfo(rowData);
                    isSimulationSelected.set(true);
                }
            });
            return row;
        });
    }

    public void setClientMainController(ClientMainController clientMainController) {
        this.clientMainController = clientMainController;
    }

    public BorderPane getDetailsBorderPane() {
        return detailsBorderPane;
    }

    @FXML
    void clearDetails(ActionEvent event) {
        detailsFlowPane.getChildren().clear();
        detailsBorderPane.setCenter(detailsScrollPane);
    }


    @FXML
    void showEntities(ActionEvent event) throws Exception {
        clearDetails(event);

        for (EntityDTO dto: simulationDetails.getEntitiesList()) {
            URL entityDetailsFXML = getClass().getResource("/predictions/client/components/details/entity/entityDetails.fxml");
            FXMLLoader loader = new FXMLLoader(entityDetailsFXML);
            GridPane entityCard = loader.load();

            EntityCardController entityCardController = loader.getController();
            entityCardController.setName(dto.getEntityName());
            entityCardController.setProperties(dto.getPropertiesList());
            entityCard.getStylesheets().add("/predictions/client//components/details/entity/entity.css");

            detailsFlowPane.getChildren().add(entityCard);
        }
    }

    @FXML
    void showEnvVariables(ActionEvent event) throws Exception {
        clearDetails(event);

        for (PropertyDTO dto : simulationDetails.getEnvVariablesList()) {
            URL envVariableDetailsFXML = getClass().getResource("/predictions/client/components/details/environment/variable/environmentVariableDetails.fxml");
            FXMLLoader loader = new FXMLLoader(envVariableDetailsFXML);
            GridPane envVariableCard = loader.load();

            EnvVariableCardController envVariableCardController = loader.getController();
            envVariableCardController.setEnvVariableCard(dto);
            envVariableCard.getStylesheets().add("/predictions/client/components/details/environment/variable/envVariables.css");

            detailsFlowPane.getChildren().add(envVariableCard);
        }
    }

    @FXML
    void showGrid(ActionEvent event) throws Exception{
        clearDetails(event);

        URL gridDetailsFXML = getClass().getResource("/predictions/client/components/details/grid/gridDetails.fxml");
        FXMLLoader loader = new FXMLLoader(gridDetailsFXML);
        GridPane gridCard = loader.load();

        GridCardController gridCardController = loader.getController();
        gridCardController.setAxisLabels(simulationDetails.getGrid().getX(), simulationDetails.getGrid().getY());
        gridCard.getStylesheets().add("/predictions/client/components/details/grid/grid.css");

        detailsFlowPane.getChildren().add(gridCard);
    }

    @FXML
    void showRules(ActionEvent event) throws Exception {
        clearDetails(event);

        URL rulesManagerDetailsFXML = getClass().getResource("/predictions/client/components/details/rules/manager/rulesManager.fxml");
        FXMLLoader loader = new FXMLLoader(rulesManagerDetailsFXML);
        BorderPane rulesManager = loader.load();

        RulesManagerController rulesManagerController = loader.getController();
        rulesManagerController.showRuleCards(simulationDetails.getRulesList());
        detailsBorderPane.setCenter(rulesManager);
    }

    void sendRequestForSimulationInfo(String simulationName) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(PREFIX_BASE_URL + "/details").newBuilder();
        urlBuilder.addQueryParameter("name", simulationName);
        String finalUrl = urlBuilder.build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showAlert("HTTP error: " + e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Platform.runLater(() -> {
                    try {
                        simulationDetails = GSON_INSTANCE.fromJson(response.body().string(), SimulationInfoDTO.class);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });
    }

    public void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message);
            alert.setHeaderText(null);
            alert.show();
        });
    }

    public void updateSimulationList(List<String> simulationNames) {
        Platform.runLater(() -> {
            ObservableList<String> items = simulationsTable.getItems();
            items.clear();
            items.addAll(simulationNames);
        });
    }

    public void startListRefresher() {
        listRefresher = new SimulationListRefresher (
                this::updateSimulationList,
                this::showAlert);
        timer = new Timer();
        timer.schedule(listRefresher, 2000, 2000);
    }

    public void closeListRefresher() {
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

}
