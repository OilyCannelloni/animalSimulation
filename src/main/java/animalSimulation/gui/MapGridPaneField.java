package animalSimulation.gui;

import animalSimulation.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.util.LinkedList;
import java.util.ListIterator;

public class MapGridPaneField extends StackPane {
    private final LinkedList<ImageView> imageViews;
    private final App app;
    private final Vector2d position;

    public MapGridPaneField(App app, Vector2d position) {
        super();
        this.app = app;
        this.position = position;
        this.imageViews = new LinkedList<>();
        int MAX_IMAGE_VIEWS = 9;
        for (int i = 0; i < MAX_IMAGE_VIEWS; i++) {
            ImageView iv = new ImageView();
            this.imageViews.add(iv);
            this.getChildren().add(iv);
        }
        this.setOnMouseClicked(this::onClick);
    }

    private void onClick(MouseEvent mouseEvent) {
        LinkedList<IMapElement> elementsAtField = this.app.getActiveMap().ElementsAt(this.position);
        Animal bestAnimal = Algorithm.getStrongestAnimal(elementsAtField);
        if (bestAnimal == null) return;

        Simulation activeSimulation = this.app.getActiveSimulation();
        activeSimulation.setTracker(bestAnimal);
        this.app.updateStatistics();
    }

    public void clear() {
        for (ImageView v : this.imageViews) v.setImage(null);
    }

    public void update(LinkedList<IMapElement> els) {
        if (els == null) {
            this.clear();
            return;
        }

        ListIterator<ImageView> itr = this.imageViews.listIterator();
        for (IMapElement e : els) {
            itr.next().setImage(e.getImage());
        }
        while (itr.hasNext()) {
            itr.next().setImage(null);
        }
    }
}
