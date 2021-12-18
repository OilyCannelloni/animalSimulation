package animalSimulation.gui;

import animalSimulation.IMapElement;
import animalSimulation.IWorldMap;
import animalSimulation.Vector2d;
import javafx.scene.layout.*;

import java.util.LinkedList;
import java.util.Map;

public class MapGridPane extends GridPane {
    private IWorldMap map;
    private final App app;
    private MapGridPaneField[][] fields;
    private Vector2d nSquares;
    private final Vector2d fieldDimensions = new Vector2d(18, 18);
    private final ImageManager imageManager;

    public MapGridPane(App app, IWorldMap map) {
        super();
        this.app = app;
        this.map = map;
        this.imageManager = new ImageManager();
        this.nSquares = map.getBoundingBox().getDimensions();
        this.fields = new MapGridPaneField[this.nSquares.x][this.nSquares.y];
        this.configureGrid();
    }

    public void setActiveMap(IWorldMap map) {
        this.map = map;
        this.nSquares = map.getBoundingBox().getDimensions();
    }

    private void configureGrid() {
        for (int i = 0; i < this.nSquares.x; i++) {
            for (int j = 0; j < this.nSquares.y; j++) {
                this.fields[i][j] = new MapGridPaneField(this.app, new Vector2d(i, j));
                this.add(this.fields[i][j], i, j, 1, 1);
            }
        }

        for (int i = 0; i < this.nSquares.x; i++)
            this.getColumnConstraints().add(new ColumnConstraints(this.fieldDimensions.x));
        for (int i = 0; i < this.nSquares.y; i++)
            this.getRowConstraints().add(new RowConstraints(this.fieldDimensions.y));

        this.setGridLinesVisible(false);

        this.setBackground(new Background(new BackgroundImage(
                this.imageManager.getImage("jungle_background.png"),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        )));
    }

    public MapGridPaneField getField(Vector2d position) {
        return this.fields[position.x][position.y];
    }

    public void draw() {
        for (Map.Entry<Vector2d, LinkedList<IMapElement>> entry : map.getElements().entrySet()) {
            this.getField(entry.getKey()).update(entry.getValue());
        }
    }

    public void drawAll() {
        for (int i = 0; i < this.nSquares.x; i++) {
            for (int j = 0; j < this.nSquares.y; j++) {
                this.fields[i][j].update(this.map.ElementsAt(new Vector2d(i, j)));
            }
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
