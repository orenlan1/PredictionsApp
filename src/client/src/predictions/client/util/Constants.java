package predictions.client.util;

import com.google.gson.Gson;

public class Constants {
    public final static String PREFIX_BASE_URL = "http://localhost:8080/predictions";

    public final static String MAIN_FXML_LOCATION = "/predictions/client/components/main/clientMainScene.fxml";
    public final static String MAIN_CSS_LOCATION = "/predictions/client/components/main/clientMainScene.css";

    public final static String DETAILS_FXML_LOCATION = "/predictions/client/components/details/clientDetails.fxml";
    public final static String DETAILS_CSS_LOCATION = "/predictions/client/components/details/detailsSheet.css";

    public final static String RULES_DETAILS_FXML_LOCATION = "/predictions/client/components/details/rules/manager/rule/ruleDetails.fxml";
    public final static String RULES_DETAILS_CSS_LOCATION = "/predictions/client/components/details/detailsSheet.css";

    public final static String LOGIN_PAGE_FXML_LOCATION = "/predictions/client/components/login/loginScene.fxml";
    public final static Gson GSON_INSTANCE = new Gson();
}
