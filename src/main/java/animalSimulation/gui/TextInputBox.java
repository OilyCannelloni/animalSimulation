package animalSimulation.gui;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class TextInputBox extends VBox {
    private final Text text;
    private final TextField field;

    public TextInputBox(String label, String defaultVal) {
        super();
        this.text = new Text(label);
        this.field = new TextField(defaultVal);
        this.field.setPrefWidth(100);
        this.setPrefWidth(150);
        this.getChildren().addAll(this.text, this.field);
    }

    public String getValue() {
        return this.field.getText();
    }

    public String getLabel() {
        return this.text.getText();
    }
}
