package predictions.client.util;

import com.google.gson.Gson;

public class Constants {

    public final static String PREFIX_BASE_URL = "http://localhost:8080/predictions";
    public final static String MAIN_FXML_LOCATION = "/predictions/client/components/main/adminMainScene.fxml";
    public final static String MANAGEMENT_FXML_LOCATION =  "/predictions/client/components/management/management.fxml";


    public final static String RULES_DETAILS_FXML_LOCATION = "/predictions/client/components/management/details/rules/manager/rule/ruleDetails.fxml";

    public final static String RULES_DETAILS_CSS_LOCATION = "/predictions/client/components/management/details/rules/manager/rule/rule.css";
    public final static String MAIN_CSS_LOCATION = "/predictions/client/components/main/adminMainScene.css";
    public final static String MANAGEMENT_CSS_LOCATION = "/predictions/client/components/management/management.css";


    public final static Gson GSON_INSTANCE = new Gson();



}
