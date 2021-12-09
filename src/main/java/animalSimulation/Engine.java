package animalSimulation;

import animalSimulation.gui.ImageManager;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Engine {
    private final JungleMap map;
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory, junglePlantFactory;

    public Engine(JungleMap map, ImageManager imageManager) {
        this.map = map;
        LinkedList<IPositionChangeObserver> observers = new LinkedList<>();
        this.animalFactory = new AnimalFactory(map, imageManager, 50, 1);
        this.plantFactory = new PlantFactory(map, imageManager, false, 20);
        this.junglePlantFactory = new PlantFactory(map, imageManager, true, 20);
    }

    public void processTurn() {
        // remove dead bodies
        this.removeDead();

        // turn and move animals
        this.makeAnimalMoves();

        // eat plants
        this.eatPlants();

        // reproduce animals
        this.reproduceAnimals();

        // place plants
        this.growPlants();
    }

    private void reproduceAnimals() {
        LinkedList<Animal> newAnimals = new LinkedList<>();

        for (Vector2d position : this.getAnimalPositions()) {
            LinkedList<IMapElement> elements = this.map.ElementsAt(position);
            LinkedList<Animal> animals = new LinkedList<>();

            for (IMapElement e : elements) {
                if (!(e instanceof Animal)) continue;
                animals.add((Animal) e);
            }
            if (animals.size() < 2) continue;

            animals.sort(Comparator.comparingInt(Animal::getEnergy));

            Animal a1 = animals.removeLast(), a2 = animals.removeLast();

            if (a2.getEnergy() > animalFactory.startEnergy / 2){
                newAnimals.add(this.animalFactory.create(a1, a2));
            }
        }

        newAnimals.forEach(this.map::placeElement);
    }

    private void makeAnimalMoves() {
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                a.makeMove();
            }
        }
    }

    private void removeDead() {
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                if (a.getEnergy() <= 0) map.removeElement(a);
            }
        }
    }

    private void growPlants() {
        Vector2d jungleField = Algorithm.getRandomEmptyField(this.map, this.map.getJungleBox());
        if (jungleField != null) this.junglePlantFactory.createPlace(jungleField);
        Vector2d plantField = Algorithm.getRandomEmptyFieldOutside(this.map, this.map.getJungleBox());
        if (plantField != null) this.plantFactory.createPlace(plantField);
    }

    private void eatPlants() {
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
    }

    private Set<Vector2d> getAnimalPositions() {
        Set<Vector2d> res = new HashSet<>();
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                res.add(e.getPosition());
            }
        }
        return res;
    }

    public void endTurn() {
        this.map.clearUpdatedFields();
    }
}
