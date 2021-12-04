package animalSimulation;

import javax.swing.*;

public class Plant extends AbstractMapElement {
    public Plant(IWorldMap map, Vector2d position) {
        super(map, position);
    }

    @Override
    public ImageIcon getIcon() {
        return null;
    }
}
