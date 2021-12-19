package animalSimulation.gui;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

import java.util.HashMap;

public class MapConfigurationBox extends HBox {
    private final ComboBox<String> mapTypeSelect;
    private TextInputBox mapNameInput;
    private int mapsN = 1;

    public MapConfigurationBox(String[] mapTypes, int mapID){
        super();

        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefHeight(50);

        this.mapTypeSelect = new ComboBox<>();
        this.mapTypeSelect.setItems(FXCollections.observableArrayList(mapTypes));
        this.mapTypeSelect.setPrefWidth(200);
        this.mapTypeSelect.setValue(mapTypes[0]);

        this.mapNameInput = new TextInputBox("Map Name", "map" + mapID);

        this.getChildren().addAll(
                this.mapTypeSelect,
                this.mapNameInput,
                new NumberInputBox("Height", 30),
                new NumberInputBox("Width", 100),
                new NumberInputBox("Jungle %", 10),
                new NumberInputBox("Respawn Threshold", 5),
                new NumberInputBox("Respawn Copies", 1),
                new NumberInputBox("Respawn Repeat", 3),
                new NumberInputBox("Start Energy", 200),
                new NumberInputBox("Move Energy", 1),
                new NumberInputBox("Initial Animals", 20),
                new NumberInputBox("Plant Energy", 60)
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
