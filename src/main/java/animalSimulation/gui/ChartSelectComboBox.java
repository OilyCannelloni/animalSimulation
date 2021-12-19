package animalSimulation.gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;


public class ChartSelectComboBox extends ComboBox<String> {
    public ChartSelectComboBox(ObservableList<String> items, EventHandler<ActionEvent> event) {
        this.setItems(items);
        this.setPrefWidth(200);
        this.setOnAction(event);
    }
}
