package animalSimulation.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class ToggleButton extends Button {
    String active;
    String inactive;
    public boolean isActive;

    public ToggleButton(EventHandler<ActionEvent> onClick, String active, String inactive) {
        super();
        this.active = active;
        this.inactive = inactive;
        this.setText(this.inactive);
        this.setPrefSize(400, 100);
        this.setOnAction((e) -> {
            this.toggle();
            onClick.handle(e);
        });
    }

    public void toggle() {
        if (this.isActive) {
            this.setText(this.inactive);
        } else {
            this.setText(this.active);
        }
        this.isActive = !this.isActive;
    }

    public void setActive() {
        this.setText(this.active);
    }

    public void setInactive() {
        this.setText(this.inactive);
    }
}
