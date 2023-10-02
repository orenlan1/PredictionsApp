package predictions.client.components.management.details.rules.manager.rule;

import predictions.client.components.management.details.rules.manager.RulesManagerController;
import predictions.client.components.management.details.rules.manager.action.CardController;
import predictions.client.components.management.details.rules.manager.action.calculation.MultiplicationDivisionCardController;
//import components.management.details.rules.manager.RulesManagerController;
//import components.details.rules.manager.action.CardController;
//import components.details.rules.manager.action.calculation.MultiplicationDivisionCardController;
//import components.details.rules.manager.action.increase.IncreaseDecreaseCardController;
//import components.details.rules.manager.action.kill.KillCardController;
import dto.ActionDTO;
import dto.RuleDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;

public class RuleCardController {

    @FXML
    private GridPane ruleCardGridPane;

    @FXML
    private VBox actionList;

    @FXML
    private Label nameLabel;

    @FXML
    private Label numActionsLabel;

    @FXML
    private Label probabilityLabel;

    @FXML
    private Label ticksLabel;

    private RulesManagerController rulesManagerController;


    public void setRulesManagerController(RulesManagerController rulesManagerController) {
        this.rulesManagerController = rulesManagerController;
    }

    public void setRuleCard(RuleDTO dto) throws Exception {
        setNameLabel(dto.getRuleName());
        setNumActionsLabel(dto.getActionsNumber());
        setProbabilityLabel(dto.getProbability());
        setTicksLabel(dto.getTicks());
        setActionList(dto.getActionsDTOs());
    }

    public void setNameLabel(String name) {
        nameLabel.textProperty().set("Name: " + name);
    }

    public void setNumActionsLabel(Integer n) {
        numActionsLabel.textProperty().set("Actions: " + n);
    }

    public void setProbabilityLabel(Double probability) {
        probabilityLabel.textProperty().set("Probability: " + String.format("%.2f" , probability));
    }

    public void setTicksLabel(Integer ticks) {
        ticksLabel.textProperty().set("Ticks: " + ticks);
    }

    public void setActionList(List<ActionDTO> DTOs) {
        for (ActionDTO dto : DTOs) {
            Button actionButton = new Button();
            actionButton.textProperty().set(dto.getType());

            actionButton.setOnAction(event -> {
                try {
                    showAction(dto);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            actionList.getChildren().add(actionButton);
        }
    }

    public void showAction(ActionDTO dto) throws Exception {
        URL actionDetailsFXML = getClass().getResource("predicitons/client/components/management/details/rules/manager/action/increase/increaseAndDecreaseActionDetails.fxml");
        FXMLLoader loader;
        GridPane actionCard;
        CardController controller;

        String type = dto.getType();
        switch (type) {
            case "INCREASE":
            case "DECREASE":
                actionDetailsFXML = getClass().getResource("/predictions/client/components/management/details/rules/manager/action/increase/increaseAndDecreaseActionDetails.fxml");
                break;
            case "MULTIPLICATION":
            case "DIVISION":
                actionDetailsFXML = getClass().getResource("/predictions/client/components/management/details/rules/manager/action/calculation/calculationActionDetails.fxml");
                break;
            case "CONDITION":
                if (dto.getConditionActionDTO().isSingular())
                    actionDetailsFXML = getClass().getResource("/predictions/client/components/management/details/rules/manager/action/condition/singular/singleConditionActionDetails.fxml");
                else
                    actionDetailsFXML = getClass().getResource("/predictions/client/components/management/details/rules/manager/action/condition/multiple/multipleConditionActionDetails.fxml");
                break;
            case "SET":
                actionDetailsFXML = getClass().getResource("/predictions/client/components/management/details/rules/manager/action/set/setActionDetails.fxml");
                break;
            case "KILL":
                actionDetailsFXML = getClass().getResource("/predictions/client/components/management/details/rules/manager/action/kill/killActionDetails.fxml");
                break;
            case "PROXIMITY":
                actionDetailsFXML = getClass().getResource("/predictions/client/components/management/details/rules/manager/action/proximity/proximityActionDetails.fxml");
                break;
            case "REPLACE":
                actionDetailsFXML = getClass().getResource(("/predictions/client/components/management/details/rules/manager/action/replace/replaceActionDetails.fxml"));
                break;
        }

        loader = new FXMLLoader(actionDetailsFXML);
        actionCard = loader.load();
        controller = loader.getController();
        controller.setCard(dto);
        actionCard.getStylesheets().add("/predictions/client/components/management/details/rules/manager/action/action.css");

        rulesManagerController.showActionCard(actionCard);
    }
}
