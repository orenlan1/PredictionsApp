package predictions.client.components.management.details.entity;

import dto.PropertyDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class EntityCardController {

    @FXML
    private Label name;

    @FXML
    private VBox propertiesList;

    @FXML
    private GridPane entityCardGridPane;

    public void setName(String entityName) {
        name.textProperty().set("Name: " + entityName);
    }

    public void setProperties(List<PropertyDTO> DTOs) {
        int i = 1;
        for (PropertyDTO dto : DTOs) {
            Label label = new Label();
            String type = dto.getPropertyType();
            if (type.equals("decimal"))
                label.textProperty().set(i + ". " + dto.getPropertyName() + ", type: " + type + ", from " + dto.getFrom().intValue() + " to " + dto.getTo().intValue());
            else if (type.equals("float")) {
                label.textProperty().set(i + ". " + dto.getPropertyName() + ", type: " + type + ", from " + dto.getFrom() + " to " + dto.getTo());
            }
            else {
                label.textProperty().set(i + ". " + dto.getPropertyName() + ", type: " + type);
            }
            propertiesList.getChildren().add(label);
            i++;
        }
    }
}
