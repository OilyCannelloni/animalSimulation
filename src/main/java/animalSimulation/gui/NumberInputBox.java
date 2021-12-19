package animalSimulation.gui;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NumberInputBox extends VBox {
    private final Text text;
    private NumberTextField field;

    public NumberInputBox(String label, int defaultVal) {
        super();
        this.text = new Text(label);
        this.field = new NumberTextField(defaultVal);
        this.field.setPrefWidth(100);
        this.setPrefWidth(150);
        this.getChildren().addAll(this.text, this.field);
    }

    public int getValue() {
        return this.field.getValue();
    }

    public String getLabel() {
        return this.text.getText();
    }
}
