package predictions.client.components.details.rules.manager.action.set;

import dto.ActionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import predictions.client.components.details.rules.manager.action.CardController;

public class SetCardController implements CardController {

    @FXML
    private Label mainEntityLabel;

    @FXML
    private Label secondaryEntityLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label valueLabel;

    @Override
    public void setCard(ActionDTO dto) {
        setTypeLabel(dto.getType());
        setMainEntityLabel(dto.getPrimaryEntity());
        setSecondaryEntityLabel(dto.getSecondaryEntity(), dto.isSecondaryEntity());
        setValueLabel(dto.getArgs().get(0));
    }

    public void setValueLabel(String value) {
        valueLabel.textProperty().set("Value: " + value);
    }

    public void setMainEntityLabel(String entity) {
        mainEntityLabel.textProperty().set("Main entity: " + entity);
    }

    public void setSecondaryEntityLabel(String entity, boolean secondary) {
        if (secondary)
            secondaryEntityLabel.textProperty().set("Secondary entity: " + entity);
        else
            secondaryEntityLabel.textProperty().set("No secondary entity");
    }

    public void setTypeLabel(String type) {
        typeLabel.textProperty().set("Type: " + type);
    }
}
