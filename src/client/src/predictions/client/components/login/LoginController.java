package predictions.client.components.login;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import predictions.client.components.main.ClientMainController;
import predictions.client.util.http.HttpClientUtil;

import java.io.IOException;
import static predictions.client.util.Constants.PREFIX_BASE_URL;

public class LoginController {

    @FXML
    private TextField clientNameField;

    @FXML
    private Button loginButton;

    @FXML
    private Button quitButton;

    @FXML
    private GridPane loginPage;

    private ClientMainController clientMainController;


    public void setClientMainController(ClientMainController clientMainController) {
        this.clientMainController = clientMainController;
    }

    @FXML
    public void initialize() {
        clientNameField.setOnKeyPressed( event -> {
            if(event.getCode() == KeyCode.ENTER) {
                loginClicked(new ActionEvent());
            }
        });
    }

    public GridPane getLoginPage() {
        return loginPage;
    }

    @FXML
    void loginClicked(ActionEvent event) {
        String userName = clientNameField.getText();
        if (userName.isEmpty()) {
            showAlert("User name is empty. You can't login with empty user name");
            return;
        }

        String finalUrl = HttpUrl
                .parse(PREFIX_BASE_URL + "/login")
                .newBuilder()
                .addQueryParameter("username", userName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        showAlert("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            showAlert("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        clientMainController.clearMainPanel();
                        clientMainController.setNameLabel(userName);
                        clientMainController.setDisableButtons(false);
                    });
                }
            }
        });

    }

    @FXML
    void quitClicked(ActionEvent event) {
        Platform.exit();
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setHeaderText(null);
        alert.show();
    }

}
