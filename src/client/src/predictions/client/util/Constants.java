package predictions.client.util;

import com.google.gson.Gson;

public class Constants {
    public final static String PREFIX_BASE_URL = "http://localhost:8080/predictions";
    public final static String MAIN_FXML_LOCATION = "/predictions/client/components/main/clientMainScene.fxml";
    public final static String MAIN_CSS_LOCATION = "/predictions/client/components/main/clientMainScene.css";
    public final static String LOGIN_PAGE_FXML_LOCATION = "/predictions/client/components/login/loginScene.fxml";
    public final static Gson GSON_INSTANCE = new Gson();
}
