package predictions.client.components.details.environment.variable;

import dto.PropertyDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class EnvVariableCardController {

    @FXML
    private GridPane envVariableCardGridPane;

    @FXML
    private Label fromLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label toLabel;

    @FXML
    private Label typeLabel;

    @FXML
    public void initialize() {
        toLabel.textProperty().set("");
        fromLabel.textProperty().set("");
    }

    public void setEnvVariableCard(PropertyDTO dto) {
        String type = dto.getPropertyType();
        setNameLabel(dto.getPropertyName());
        setTypeLabel(type);

        if (type.equals("decimal") || type.equals("float")) {
            setToLabel(dto.getTo(), type);
            setFromLabel(dto.getFrom(), type);
        }
    }

    public void setNameLabel(String name) {
        nameLabel.textProperty().set("Name: " + name);
    }

    public void setTypeLabel(String type) {
        typeLabel.textProperty().set("Type: " + type);
    }

    public void setToLabel(Double to, String type) {
        if (type.equals("decimal"))
            toLabel.textProperty().set("To: " + to.intValue());
        else
            toLabel.textProperty().set("To: " + String.format("%.2f", to));
    }

    public void setFromLabel(Double from, String type) {
        if (type.equals("decimal"))
            fromLabel.textProperty().set("From: " + from.intValue());
        else
            fromLabel.textProperty().set("From: " + String.format("%.2f", from));
    }
}
