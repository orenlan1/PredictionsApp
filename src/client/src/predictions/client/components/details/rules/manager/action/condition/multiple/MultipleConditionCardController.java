package predictions.client.components.details.rules.manager.action.condition.multiple;

import dto.ActionDTO;
import dto.ConditionActionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import predictions.client.components.details.rules.manager.action.CardController;

public class MultipleConditionCardController implements CardController {

    @FXML
    private Label logicLabel;

    @FXML
    private Label elseActionsLabel;

    @FXML
    private Label subActonsLabel;

    @FXML
    private Label mainEntityLabel;

    @FXML
    private Label secondaryEntityLabel;

    @FXML
    private Label thenActionsLabel;

    @FXML
    private Label typeLabel;

    @Override
    public void setCard(ActionDTO dto) {
        ConditionActionDTO conditionDto = dto.getConditionActionDTO();
        setTypeLabel(dto.getType());
        setMainEntityLabel(dto.getPrimaryEntity());
        setSecondaryEntityLabel(dto.getSecondaryEntity());
        setLogicLabel(dto.getArgs().get(0));
        setSubThenElseLabels(conditionDto.getNumOfSubConditions(), conditionDto.getThenActions(), conditionDto.getElseActions());
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

    public void setSubThenElseLabels(Integer subNum, Integer thenNum, Integer elseNum) {
        subActonsLabel.textProperty().set("Sub actions number: " + subNum);
        thenActionsLabel.textProperty().set("Then actions number: " + thenNum);
        elseActionsLabel.textProperty().set("Else actions number: " + elseNum);
    }

    public void setLogicLabel(String logic) {
        logicLabel.textProperty().set("Logic: " + logic);
    }
}
