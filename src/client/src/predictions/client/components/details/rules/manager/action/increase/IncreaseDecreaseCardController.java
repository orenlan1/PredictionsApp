package predictions.client.components.details.rules.manager.action.increase;

import dto.ActionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import predictions.client.components.details.rules.manager.action.CardController;

public class IncreaseDecreaseCardController implements CardController {

    @FXML
    private Label byLabel;

    @FXML
    private Label mainEntityLabel;

    @FXML
    private Label secondaryEntityLabel;

    @FXML
    private Label typeLabel;

    @Override
    public void setCard(ActionDTO dto) {
        setTypeLabel(dto.getType());
        setMainEntityLabel(dto.getPrimaryEntity());
        setSecondaryEntityLabel(dto.getSecondaryEntity());
        setByLabel(dto.getArgs().get(0));
    }

    public void setByLabel(String by) {
        byLabel.textProperty().set("By: " + by);
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

}
