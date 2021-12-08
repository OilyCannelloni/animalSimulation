package animalSimulation.gui;

import animalSimulation.IMapElement;
import animalSimulation.IWorldMap;
import animalSimulation.Vector2d;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.ImageView;

import java.util.LinkedList;

public class MapGridPane extends GridPane {
    private IWorldMap map;
    private ImageView[][] fields;
    private final Vector2d dimensions, nSquares, fieldDimensions = new Vector2d(18, 18);
    private final ImageManager imageManager;

    public MapGridPane(IWorldMap map) {
        super();
        this.map = map;
        this.imageManager = new ImageManager();
        this.nSquares = map.getBoundingBox().getDimensions();
        this.dimensions = this.nSquares.multiplyEach(fieldDimensions);
        this.fields = new ImageView[this.nSquares.x][this.nSquares.y];

        this.configureGrid();
    }

    private void configureGrid() {
        for (int i = 0; i < this.nSquares.x; i++) {
            for (int j = 0; j < this.nSquares.y; j++) {
                this.fields[i][j] = new ImageView();
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

    public void draw() {
        for (LinkedList<IMapElement> field : map.getElements().values()) {
            for (IMapElement e : field) {
                Vector2d position = e.getPosition();
                Image img = e.getImage();
                this.fields[position.x][position.y].setImage(img);
            }
        }
    }
}
