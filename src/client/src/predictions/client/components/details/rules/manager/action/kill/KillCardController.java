package predictions.client.components.details.rules.manager.action.kill;

import dto.ActionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import predictions.client.components.details.rules.manager.action.CardController;

public class KillCardController implements CardController {

    @FXML
    private Label mainEntityLabel;

    @FXML
    private Label typeLabel;

    @Override
    public void setCard(ActionDTO dto) {
        setTypeLabel(dto.getType());
        setMainEntityLabel(dto.getPrimaryEntity());
    }

    public void setMainEntityLabel(String entity) {
        mainEntityLabel.textProperty().set("Main entity: " + entity);
    }

    public void setTypeLabel(String type) {
        typeLabel.textProperty().set("Type: " + type);
    }
}
