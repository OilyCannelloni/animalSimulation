package animalSimulation.gui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.util.Arrays;

public class StatDisplayBox extends HBox {
    private final Text valueText;

    public StatDisplayBox(String statName) {
        super();
        StackPane nameBox = new StackPane();
        nameBox.setPrefWidth(200);
        nameBox.getChildren().add(new Text(statName));

        this.valueText = new Text();

        StackPane valueBox = new StackPane();
        valueBox.setPrefWidth(200);
        valueBox.getChildren().add(this.valueText);

        this.getChildren().addAll(nameBox, valueBox);
        this.setValue(0);
    }

    public void setValue(int value) {
        this.valueText.setText(Integer.toString(value));
    }

    public void setValue(String value) {
        this.valueText.setText(value);
    }
}
