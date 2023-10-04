package predictions.client.components.details.grid;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GridCardController {

    @FXML
    private GridPane gridCardGridPane;

    @FXML
    private Label xAxisLabel;

    @FXML
    private Label yAxisLabel;

    public void setAxisLabels(Integer x, Integer y) {
        setXAxisLabel(x);
        setYAxisLabel(y);
    }

    public void setXAxisLabel(Integer x) {
        xAxisLabel.textProperty().set("X axis size: " + x);
    }

    public void setYAxisLabel(Integer y) {
        yAxisLabel.textProperty().set("Y axis size: " + y);
    }
}
