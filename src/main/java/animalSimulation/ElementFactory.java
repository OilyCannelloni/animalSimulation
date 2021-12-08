package animalSimulation;

import animalSimulation.gui.ImageManager;

public abstract class ElementFactory {
    protected IWorldMap map;
    protected ImageManager imageManager;
    public ElementFactory(IWorldMap map, ImageManager imageManager) {
        this.map = map;
        this.imageManager = imageManager;
    }
}
