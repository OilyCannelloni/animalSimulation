package animalSimulation;

import animalSimulation.gui.ImageManager;

import java.util.LinkedList;

public class Engine {
    private final JungleMap map;
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory, junglePlantFactory;
    private final ImageManager imageManager;

    public Engine(JungleMap map, ImageManager imageManager) {
        this.map = map;
        this.imageManager = imageManager;
        LinkedList<IPositionChangeObserver> observers = new LinkedList<>();
        this.animalFactory = new AnimalFactory(map, imageManager, 50, 1);
        this.plantFactory = new PlantFactory(map, imageManager, false, 20);
        this.junglePlantFactory = new PlantFactory(map, imageManager, true, 40);
    }

    public void processTurn() {
        // remove dead bodies
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                if (a.getEnergy() <= 0) map.removeElement(a);
            }
        }

        // turn and move animals
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                a.makeMove();
            }
        }

        // place plants
        this.junglePlantFactory.createPlace(Algorithm.getRandomEmptyField(this.map, this.map.getJungleBox()));
        this.plantFactory.createPlace(Algorithm.getRandomEmptyFieldOutside(this.map, this.map.getJungleBox()));

    }

    public void endTurn() {
        this.map.clearUpdatedFields();
    }
}
