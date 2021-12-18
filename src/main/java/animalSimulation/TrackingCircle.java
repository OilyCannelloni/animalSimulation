package animalSimulation;

import animalSimulation.gui.ImageManager;
import javafx.scene.image.Image;

public class TrackingCircle extends AbstractMapElement {
    public TrackingCircle(IWorldMap map, Vector2d position) {
        super(map, position);
    }

    @Override
    public Image getImage() {
        return ImageManager.trackingCircleImage;
    }
}
