package animalSimulation;

import animalSimulation.gui.ImageManager;

import java.util.List;

public abstract class MovableElementFactory extends ElementFactory {
    protected List<IPositionChangeObserver> observers;

    public MovableElementFactory(IWorldMap map, ImageManager imageManager, List<IPositionChangeObserver> observers) {
        super(map, imageManager);
        this.observers = observers;
    }
}
