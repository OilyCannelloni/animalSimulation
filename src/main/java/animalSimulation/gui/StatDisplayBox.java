package animalSimulation.gui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class StatDisplayBox extends HBox {
    private final Text valueText;

    public StatDisplayBox(String statName) {
        super();
        StackPane nameBox = new StackPane();
        nameBox.setPrefWidth(200);
        nameBox.getChildren().add(new Text(statName));

        this.valueText = new Text();

        StackPane valueBox = new StackPane();
        valueBox.getChildren().add(this.valueText);
        this.valueText.wrappingWidthProperty().setValue(200);
        valueBox.setPrefWidth(100);

        this.getChildren().addAll(nameBox, valueBox);
        this.setValue(0);
        this.setMaxWidth(400);
    }

    public void setValue(int value) {
        this.valueText.setText(Integer.toString(value));
    }

    public void setValue(String value) {
        this.valueText.setText(value);
    }
}
