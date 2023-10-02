package predictions.client.components.management.details.rules.manager.action.proximity;

import predictions.client.components.management.details.rules.manager.action.CardController;

//import components.details.rules.manager.action.CardController;
import dto.ActionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProximityCardController implements CardController {

    @FXML
    private Label depthLabel;

    @FXML
    private Label mainEntityLabel;

    @FXML
    private Label secondaryEntityLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label numOfActionsLabel;

    @Override
    public void setCard(ActionDTO dto) {
        setTypeLabel(dto.getType());
        setMainEntityLabel(dto.getPrimaryEntity());
        setSecondaryEntityLabel(dto.getSecondaryEntity());
        setDepthLabel(dto.getArgs().get(0));
        setNumOfActionsLabel(dto.getArgs().get(1));
    }

    public void setDepthLabel(String depth) {
        depthLabel.textProperty().set("Depth: " + depth);
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

    public void setNumOfActionsLabel(String numOfActions) {
        numOfActionsLabel.textProperty().set("Number of actions: " + numOfActions);
    }
}