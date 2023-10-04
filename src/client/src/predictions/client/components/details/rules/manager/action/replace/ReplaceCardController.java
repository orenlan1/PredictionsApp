package predictions.client.components.details.rules.manager.action.replace;

import dto.ActionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import predictions.client.components.details.rules.manager.action.CardController;

public class ReplaceCardController implements CardController {

    @FXML
    private Label mainEntityLabel;

    @FXML
    private Label modeLabel;

    @FXML
    private Label secondaryEntityLabel;

    @FXML
    private Label typeLabel;

    @Override
    public void setCard(ActionDTO dto) {
        setTypeLabel(dto.getType());
        setMainEntityLabel(dto.getPrimaryEntity());
        setSecondaryEntityLabel(dto.getSecondaryEntity());
        setModeLabel(dto.getArgs().get(0));
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

    public void setModeLabel(String mode) {
        modeLabel.textProperty().set("Mode: " + mode);
    }
}
