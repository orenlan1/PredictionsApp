package predictions.client.components.details.rules.manager.action.calculation;

import dto.ActionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import predictions.client.components.details.rules.manager.action.CardController;

public class MultiplicationDivisionCardController implements CardController {

    @FXML
    private Label arg1Label;

    @FXML
    private Label arg2Label;

    @FXML
    private Label mainEntityLabel;

    @FXML
    private Label secondaryEntityLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private ImageView calculationIcon;

    @Override
    public void setCard(ActionDTO dto) {
        setArgsLabels(dto.getArgs().get(0), dto.getArgs().get(1));
        setMainEntityLabel(dto.getPrimaryEntity());
        setSecondaryEntityLabel(dto.getSecondaryEntity());
        setTypeLabel(dto.getType());
    }

    public void setArgsLabels(String arg1, String arg2) {
        arg1Label.textProperty().set("Arg 1: " + arg1);
        arg2Label.textProperty().set("Arg 2: " + arg2);
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
        if (type.equals("DIVISION")){
            calculationIcon.setImage(new Image("/predictions/client/components/management/details/rules/manager/action/calculation/divisionIcon.png"));
        }
    }
}
