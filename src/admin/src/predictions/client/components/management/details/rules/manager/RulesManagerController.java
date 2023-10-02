package predictions.client.components.management.details.rules.manager;

import predictions.client.components.management.details.rules.manager.rule.RuleCardController;
//import components.details.DetailsController;
//import components.details.rules.manager.rule.RuleCardController;
import dto.RuleDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.List;

import static predictions.client.util.Constants.RULES_DETAILS_FXML_LOCATION;
import static predictions.client.util.Constants.RULES_DETAILS_CSS_LOCATION;
public class RulesManagerController {

    @FXML
    private BorderPane rulesAndActionsContainer;

    @FXML
    private FlowPane actionsContainer;

    @FXML
    private FlowPane rulesContainer;

    @FXML
    public void initialize() {
        HBox.setHgrow(rulesContainer, Priority.ALWAYS);
        HBox.setHgrow(actionsContainer, Priority.ALWAYS);
    }

    public void showRuleCards(List<RuleDTO> DTOs) throws Exception {
        rulesContainer.getChildren().clear();

        for (RuleDTO dto : DTOs) {
            URL ruleDetailsFXML = getClass().getResource(RULES_DETAILS_FXML_LOCATION);
            FXMLLoader loader = new FXMLLoader(ruleDetailsFXML);
            GridPane ruleCard = loader.load();

            RuleCardController ruleCardController = loader.getController();
            ruleCardController.setRulesManagerController(this);
            ruleCardController.setRuleCard(dto);
            ruleCard.getStylesheets().add(RULES_DETAILS_CSS_LOCATION);

            rulesContainer.getChildren().add(ruleCard);
        }
    }

    public void showActionCard(GridPane actionCard) {
        actionsContainer.getChildren().clear();
        actionsContainer.getChildren().add(actionCard);
    }
}
