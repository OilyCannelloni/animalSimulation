package animalSimulation;

import javafx.scene.image.Image;

public interface IMapElement {
    Vector2d getPosition();

    void interactWith(IMapElement other);

    Image getImage();

    String toString();
}
