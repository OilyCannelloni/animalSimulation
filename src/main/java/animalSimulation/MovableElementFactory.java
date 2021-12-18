package animalSimulation;

import animalSimulation.gui.ImageManager;

import java.util.List;

public abstract class MovableElementFactory extends ElementFactory {
    protected List<IActionObserver> observers;

    public MovableElementFactory(IWorldMap map, ImageManager imageManager, List<IActionObserver> observers) {
        super(map, imageManager);
        this.observers = observers;
    }
}
