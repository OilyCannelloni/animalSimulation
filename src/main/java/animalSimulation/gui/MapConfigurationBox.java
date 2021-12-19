package animalSimulation.gui;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Pair;

import java.util.HashMap;

public class MapConfigurationBox extends HBox {
    private final ComboBox<String> mapTypeSelect;
    private TextInputBox mapNameInput;

    public MapConfigurationBox(String[] mapTypes){
        super();

        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefHeight(50);

        this.mapTypeSelect = new ComboBox<>();
        this.mapTypeSelect.setItems(FXCollections.observableArrayList(mapTypes));

        this.mapNameInput = new TextInputBox("Map Name", "map");

        NumberInputBox heightInput = new NumberInputBox("Height", 30);
        NumberInputBox widthInput = new NumberInputBox("Width", 80);
        NumberInputBox junglePercentageInput = new NumberInputBox("Jungle %", 10);
        NumberInputBox respawnAliveThresholdInput = new NumberInputBox("Respawn Threshold", 0);
        NumberInputBox respawnNCopiesInput = new NumberInputBox("Respawn Copies", 0);
        NumberInputBox respawnRepeatInput = new NumberInputBox("Repeat respawn", 0);

        this.getChildren().addAll(
            this.mapTypeSelect,
            heightInput,
            widthInput,
            junglePercentageInput,
            respawnAliveThresholdInput,
            respawnNCopiesInput,
            respawnRepeatInput
        );
    }

    public String getMapTypeName() {
        return this.mapTypeSelect.getValue();
    }

    public String getMapName() {
        return this.mapNameInput.getValue();
    }

    public HashMap<String, Integer> getMapData() {
        HashMap<String, Integer> mapData = new HashMap<>();
        for (Node element : this.getChildren()) {
            if (element instanceof NumberInputBox) {
                NumberInputBox numberInputBox = (NumberInputBox) element;
                mapData.put(numberInputBox.getLabel(), numberInputBox.getValue());
            }
        }

        return mapData;
    }
}
