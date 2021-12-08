package animalSimulation.gui;

import animalSimulation.IMapElement;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.LinkedList;
import java.util.ListIterator;

public class MapGridPaneField extends StackPane {
    private final LinkedList<ImageView> imageViews;

    public MapGridPaneField() {
        super();
        this.imageViews = new LinkedList<>();
        int MAX_IMAGE_VIEWS = 9;
        for (int i = 0; i < MAX_IMAGE_VIEWS; i++) {
            ImageView iv = new ImageView();
            this.imageViews.add(iv);
            this.getChildren().add(iv);
        }
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
