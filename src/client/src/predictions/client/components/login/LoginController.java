package predictions.client.components.login;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    private Label errorMessageLabel;

    @FXML
    private AnchorPane loginAnchorPane;

    private ClientMainController clientMainController;

    private final StringProperty errorMessageProperty = new SimpleStringProperty();



    @FXML
    public void initialize() {
        errorMessageLabel.textProperty().bind(errorMessageProperty);
    }


    public void setClientMainController(ClientMainController clientMainController) {
        this.clientMainController = clientMainController;
    }

    public AnchorPane getLoginAnchorPane() {
        return loginAnchorPane;
    }

    @FXML
    void loginClicked(ActionEvent event) {
        String userName = clientNameField.getText();
        if (userName.isEmpty()) {
            errorMessageProperty.set("User name is empty. You can't login with empty user name");
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
                        errorMessageProperty.set("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() ->
                            errorMessageProperty.set("Something went wrong: " + responseBody)
                    );
                } else {
                    Platform.runLater(() -> {
                        clientMainController.clearMainPanel();

                    });
                }
            }
        });

    }

    @FXML
    void quitClicked(ActionEvent event) {
        Platform.exit();
    }

}
