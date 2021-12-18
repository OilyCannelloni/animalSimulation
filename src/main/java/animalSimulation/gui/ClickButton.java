package animalSimulation.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ClickButton extends Button {
    public ClickButton(EventHandler<ActionEvent> onClick, String text) {
        super();
        this.setText(text);
        this.setPrefSize(400, 100);
        this.setOnAction(onClick);
    }
}
