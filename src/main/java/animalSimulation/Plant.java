package animalSimulation;

import javafx.scene.image.Image;

public class Plant extends AbstractMapElement {
    public Plant(IWorldMap map, Vector2d position) {
        super(map, position);
    }

    @Override
    public Image getImage() {
        return null;
    }
}
