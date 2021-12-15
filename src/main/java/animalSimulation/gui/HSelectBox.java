package animalSimulation.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class HSelectBox extends HBox {
    public int active;
    private final String activeColor = "#55AA55";
    private final String disabledColor = "#AA5555";
    private ArrayList<SelectBoxButton> buttons;

    public HSelectBox(int width, int height, ArrayList<SelectBoxButton> buttons){
        super();
        this.setWidth(width);
        this.setHeight(height);
        this.buttons = buttons;
        this.getChildren().addAll(buttons);

        for (int i = 0; i < this.buttons.size(); i++) {
            SelectBoxButton btn = this.buttons.get(i);
            int finalI = i;
            EventHandler<ActionEvent> old = btn.getOnAction();
            btn.setOnAction((a) -> {
                if (this.onClick(finalI)){
                    old.handle(a);
                }
            });
            this.setBackgroundColor(i, this.disabledColor);
        }

        this.active = 0;
        if (!this.buttons.isEmpty()) {
            this.setActive(0);
        }
    }

    public void setActive(int index) {
        this.setBackgroundColor(index, this.activeColor);
    }

    public void setDisabled(int index) {
        this.setBackgroundColor(index, this.disabledColor);
    }

    private void setBackgroundColor(int index, String color) {
        this.buttons.get(index).setStyle("-fx-background-color: " + color + ";");
    }

    public boolean onClick(int index) {
        if (index == this.active) return false;
        this.setActive(index);
        this.setDisabled(this.active);
        this.active = index;
        return true;
    }
}
