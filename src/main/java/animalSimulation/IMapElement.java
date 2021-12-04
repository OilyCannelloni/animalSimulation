package animalSimulation;

import javax.swing.*;

public interface IMapElement {
    Vector2d getPosition();

    void interactWith(IMapElement other);

    ImageIcon getIcon();

    String toString();
}
