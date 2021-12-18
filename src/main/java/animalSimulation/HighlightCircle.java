package animalSimulation;

import animalSimulation.gui.ImageManager;
import javafx.scene.image.Image;

public class HighlightCircle extends AbstractMapElement {
    public HighlightCircle(IWorldMap map, Vector2d position) {
        super(map, position);
    }

    @Override
    public Image getImage() {
        return ImageManager.highlightCircleImage;
    }
}
