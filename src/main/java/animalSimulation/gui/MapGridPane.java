package animalSimulation.gui;

import animalSimulation.IMapElement;
import animalSimulation.IWorldMap;
import animalSimulation.Vector2d;
import javafx.scene.layout.*;

import java.util.LinkedList;
import java.util.Map;

public class MapGridPane extends GridPane {
    private IWorldMap map;
    private LinkedList<IWorldMap> maps;
    private MapGridPaneField[][] fields;
    private final Vector2d dimensions, nSquares, fieldDimensions = new Vector2d(18, 18);
    private final ImageManager imageManager;

    public MapGridPane(IWorldMap map) {
        super();
        this.map = map;
        this.imageManager = new ImageManager();
        this.nSquares = map.getBoundingBox().getDimensions();
        this.dimensions = this.nSquares.multiplyEach(fieldDimensions);
        this.fields = new MapGridPaneField[this.nSquares.x][this.nSquares.y];

        this.configureGrid();
        this.setBackground(new Background(new BackgroundImage(
                this.imageManager.getImage("jungle_background.png"),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        )));
    }

    private void configureGrid() {
        for (int i = 0; i < this.nSquares.x; i++) {
            for (int j = 0; j < this.nSquares.y; j++) {
                this.fields[i][j] = new MapGridPaneField();
                this.add(this.fields[i][j], i, j, 1, 1);
            }
        }

        for (int i = 0; i < this.nSquares.x; i++)
            this.getColumnConstraints().add(new ColumnConstraints(this.fieldDimensions.x));
        for (int i = 0; i < this.nSquares.y; i++)
            this.getRowConstraints().add(new RowConstraints(this.fieldDimensions.y));

        this.setGridLinesVisible(true);
    }

    public Vector2d getDimensions() {
        return this.dimensions;
    }

    public MapGridPaneField getField(Vector2d position) {
        return this.fields[position.x][position.y];
    }

    public void draw() {
        for (Map.Entry<Vector2d, LinkedList<IMapElement>> entry : map.getElements().entrySet()) {
            this.getField(entry.getKey()).update(entry.getValue());
        }
    }

    public void clear() {
        for (int i = 0; i < this.nSquares.x; i++) {
            for (int j = 0; j < this.nSquares.y; j++) {
                this.fields[i][j].clear();
            }
        }
    }
}
