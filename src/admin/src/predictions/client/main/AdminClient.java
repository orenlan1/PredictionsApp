package predictions.client.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import predictions.client.components.main.AdminMainController;

import java.io.IOException;
import java.net.URL;

import static predictions.client.util.Constants.*;
import static predictions.client.util.Constants.MANAGEMENT_CSS_LOCATION;

public class AdminClient extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL mainFXML = getClass().getResource(MAIN_FXML_LOCATION);

        FXMLLoader mainLoader = new FXMLLoader(mainFXML);
        try {
            ScrollPane mainScrollPane = mainLoader.load();

            AdminMainController adminMainController = mainLoader.getController();

            adminMainController.setPrimaryStage(primaryStage);

            //adminMainController.initCheckAdminAlive(); //TODO remember to uncomment


            Scene scene = new Scene(mainScrollPane,1400,800);
            String mainCss = this.getClass().getResource(MAIN_CSS_LOCATION).toExternalForm();
            scene.getStylesheets().add(mainCss);

            primaryStage.setTitle("Predictions - administrator");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
