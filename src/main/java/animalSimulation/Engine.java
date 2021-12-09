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

        // eat plants
        for (IMovableElement e : this.map.getMovableElements()) {
            Vector2d pos = e.getPosition();
            LinkedList<IMapElement> elementsAtPosition = this.map.ElementsAt(pos);

            Plant plant = null;
            for (IMapElement element : elementsAtPosition) {
                if (element instanceof Plant) {
                    plant = (Plant) element;
                    break;
                }
            }
            if (plant == null) continue;

            int bestEnergy = 0;
            LinkedList<Animal> bestAnimals = new LinkedList<>();
            for (IMapElement element : elementsAtPosition) {
                if (!(element instanceof Animal)) continue;
                Animal animal = (Animal) element;

                if (animal.getEnergy() < bestEnergy) continue;
                if (animal.getEnergy() > bestEnergy) {
                    bestEnergy = animal.getEnergy();
                    bestAnimals.clear();
                }
                bestAnimals.add(animal);
            }

            int nAnimals = bestAnimals.size();
            int eatEnergy = plant.getPlantEnergy() / nAnimals;
            bestAnimals.forEach((Animal a) -> a.addEnergy(eatEnergy));

            this.map.removeElement(plant);
        }


        // place plants
        Vector2d jungleField = Algorithm.getRandomEmptyField(this.map, this.map.getJungleBox());
        if (jungleField != null) this.junglePlantFactory.createPlace(jungleField);
        Vector2d plantField = Algorithm.getRandomEmptyFieldOutside(this.map, this.map.getJungleBox());
        if (plantField != null) this.plantFactory.createPlace(plantField);
    }

    public void endTurn() {
        this.map.clearUpdatedFields();
    }
}
