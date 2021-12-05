package animalSimulation;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapVisualizer {
    private final IWorldMap map;
    private final Rect2D mapBB, gridBB;
    private int fieldHeight, fieldWidth, gridWidth, gridHeight, rows, cols;
    private final JFrame frame;
    private final JPanel gridPanel;
    private final ImageIcon background;

    private final JPanel[][] grid;

    public MapVisualizer(IWorldMap map) {
        this.map = map;
        this.fieldHeight = 24;
        this.fieldWidth = 18;
        this.mapBB = map.getBoundingBox();
        this.rows = this.mapBB.getDimensions().y;
        this.cols = this.mapBB.getDimensions().x;
        this.gridHeight = this.rows * this.fieldHeight;
        this.gridWidth = this.cols * this.fieldWidth;
        this.gridBB = new Rect2D(
                new Vector2d(0, 0),
                new Vector2d(this.gridWidth, this.gridHeight)
        );

        this.frame = new JFrame();
        this.frame.setSize(this.gridWidth, this.gridHeight);

        this.background = new ImageIcon("./src/main/resources/images/jungle1.png");
        this.gridPanel = new BGPanel(this.background);
        this.gridPanel.setLayout(new GridLayout(30, 100, 0, 0));

        this.grid = new JPanel[30][100];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.grid[i][j] = new JPanel();
                this.grid[i][j].setSize(this.fieldWidth + 2, this.fieldHeight + 2);
                this.grid[i][j].setOpaque(false);
                this.gridPanel.add(this.grid[i][j]);
            }
        }
    }

    Vector2d mapFieldToFrameXY(Vector2d mapField) {
        return new Vector2d(
                mapField.x,
                this.rows - mapField.y - 1
        );
    }

    void placeElement(IMapElement element) {
        Vector2d position = element.getPosition();
        Vector2d FrameXY = mapFieldToFrameXY(position);

        JLabel picLabel = new JLabel(element.getIcon());
        this.grid[FrameXY.y][FrameXY.x].add(picLabel);
    }

    void clearFrame() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.grid[i][j].removeAll();
                this.grid[i][j].revalidate();
                this.grid[i][j].repaint();
            }
        }
    }

    void draw() {
        this.clearFrame();
        for (List<IMapElement> list : this.map.getElements().values()) {
            for (IMapElement e : list) {
                this.placeElement(e);
            }
        }
        this.frame.add(this.gridPanel);
        this.gridPanel.revalidate();
        this.gridPanel.repaint();
        this.frame.revalidate();
        this.frame.repaint();
        this.frame.setVisible(true);
    }
}
