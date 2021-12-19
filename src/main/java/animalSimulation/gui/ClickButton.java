package animalSimulation.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ClickButton extends Button {
    public ClickButton(EventHandler<ActionEvent> onClick, String text) {
        super();
        this.setText(text);
        this.setPrefHeight(100);
        this.setPrefWidth(400);
        this.setOnAction(onClick);
    }
}
