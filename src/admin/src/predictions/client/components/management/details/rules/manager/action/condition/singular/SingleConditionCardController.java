package predictions.client.components.management.details.rules.manager.action.condition.singular;

import predictions.client.components.management.details.rules.manager.action.CardController;

//import components.details.rules.manager.action.CardController;
import dto.ActionDTO;
import dto.ConditionActionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleConditionCardController implements CardController {

    @FXML
    private Label elseActionsLabel;

    @FXML
    private Label mainEntityLabel;

    @FXML
    private Label operatorLabel;

    @FXML
    private Label propertyLabel;

    @FXML
    private Label secondaryEntityLabel;

    @FXML
    private Label thenActionsLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label valueLabel;

    @Override
    public void setCard(ActionDTO dto) {
        ConditionActionDTO conditionDto = dto.getConditionActionDTO();
        setTypeLabel(dto.getType());
        setMainEntityLabel(dto.getPrimaryEntity());
        setSecondaryEntityLabel(dto.getSecondaryEntity());
        setValueLabel(dto.getArgs().get(0));
        setOperatorLabel(dto.getArgs().get(1));
        setPropertyLabel(dto.getArgs().get(2));
        setThenElseLabels(conditionDto.getThenActions(), conditionDto.getElseActions());
    }

    public void setMainEntityLabel(String entity) {
        mainEntityLabel.textProperty().set("Main entity: " + entity);
    }

    public void setSecondaryEntityLabel(String entity) {
        if (entity != null)
            secondaryEntityLabel.textProperty().set("Secondary entity: " + entity);
        else
            secondaryEntityLabel.textProperty().set("No secondary entity");
    }

    public void setTypeLabel(String type) {
        typeLabel.textProperty().set("Type: " + type);
    }

    public void setThenElseLabels(Integer thenNum, Integer elseNum) {
        thenActionsLabel.textProperty().set("Then actions number: " + thenNum);
        elseActionsLabel.textProperty().set("Else actions number: " + elseNum);
    }

    public void setOperatorLabel(String operator) {
        operatorLabel.textProperty().set("Operator: " + operator);
    }

    public void setValueLabel(String value) {
        valueLabel.textProperty().set("Value: " + value);
    }

    public void setPropertyLabel(String propertyName) {
        propertyLabel.textProperty().set("Property: " + propertyName);
    }
}
