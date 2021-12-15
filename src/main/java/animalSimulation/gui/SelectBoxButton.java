package animalSimulation.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public class SelectBoxButton extends Button {
    public SelectBoxButton(String text, EventHandler<ActionEvent> event) {
        this.setText(text);
        this.setOnAction(event);
        this.setShape(new Rectangle(200, 100));
        this.setPrefSize(200, 100);
    }
}
