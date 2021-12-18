package animalSimulation;

import animalSimulation.gui.App;
import animalSimulation.gui.ImageManager;
import javafx.util.Pair;

import java.util.*;

public class Simulation implements Runnable {
    private final JungleMap map;
    private final AnimalFactory animalFactory;
    private final PlantFactory plantFactory, junglePlantFactory;
    public AnimalTracker tracker;

    private final App app;
    public final Object pausePauseLock, renderPauseLock;
    public boolean paused = false;
    public boolean isDisplayed = false, isBeingRendered = false;
    public int epoch = 0;

    public SimulationStatistics statistics;
    private EpochStatistics epochStatistics;

    public Simulation(App app, JungleMap map, ImageManager imageManager) {
        this.map = map;
        this.animalFactory = new AnimalFactory(map, imageManager, 50, 1);
        this.plantFactory = new PlantFactory(map, imageManager, false, 20);
        this.junglePlantFactory = new PlantFactory(map, imageManager, true, 20);
        this.app = app;
        this.pausePauseLock = new Object();
        this.renderPauseLock = new Object();
        this.statistics = new SimulationStatistics();
        this.tracker = new AnimalTracker(app, Algorithm.getRandomAnimal(map));
        this.animalFactory.factoryObservers.add(this.tracker);
    }

    @Override
    public void run() {
        while (true) {
            // wait
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) {}

            // wait for App to finish drawing
            if (this.isBeingRendered) synchronized (this.renderPauseLock) {
                try {
                    this.renderPauseLock.wait();
                } catch (InterruptedException ignore) {}
            }

            // initialize statistics
            this.epochStatistics = new EpochStatistics();

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

            // update stats
            this.epochStatistics.update(this.getDominantGenome());
            this.statistics.update(this.epochStatistics);

            // enable map update
            if (this.isDisplayed) synchronized (this.app.gridUpdatePauseLock) {
                this.app.gridUpdatePauseLock.notifyAll();
            }

            if (this.paused) synchronized (this.pausePauseLock) {
                try {
                    this.pausePauseLock.wait();
                } catch (InterruptedException ignore) {}
            }
            this.epoch++;
        }
    }

    public void setTracker(Animal animal) {
        this.animalFactory.factoryObservers.remove(this.tracker);
        this.tracker.setAnimal(animal);
        this.animalFactory.factoryObservers.add(this.tracker);
    }

    private int[] getDominantGenome() {
        Pair<int[], LinkedList<Animal>> dominantAnimals = Algorithm.getDominantGenomeAnimals(this.map);
        this.epochStatistics.epochDominantGenomeCount = dominantAnimals.getValue().size();
        return dominantAnimals.getKey();
    }


    public void togglePause() {
        this.paused = !this.paused;
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

            if (a2.getEnergy() > animalFactory.startEnergy / 1.4){
                newAnimals.add(this.animalFactory.create(a1, a2));
            }
        }

        newAnimals.forEach(this.map::placeElement);
        this.epochStatistics.epochBornCount += newAnimals.size();
    }

    private void makeAnimalMoves() {
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                this.epochStatistics.epochTotalEnergy += a.getEnergy();
                this.epochStatistics.epochTotalChildCount += a.childCount;
                a.makeMove();
                this.epochStatistics.epochAnimalCount++;
            }
        }
    }

    private void removeDead() {
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                if (a.getEnergy() <= 0) {
                    this.epochStatistics.epochDeadCount++;
                    this.epochStatistics.epochTotalDeadLifespan += a.lifespan;
                    this.animalFactory.kill(a, this.epoch);
                }
            }
        }
    }

    private void growPlants() {
        Vector2d jungleField = Algorithm.getRandomEmptyField(this.map, this.map.getJungleBox());
        if (jungleField != null) {
            this.junglePlantFactory.createPlace(jungleField);
            this.epochStatistics.epochNewPlants++;
        }
        Vector2d plantField = Algorithm.getRandomEmptyFieldOutside(this.map, this.map.getJungleBox());
        if (plantField != null) {
            this.plantFactory.createPlace(plantField);
            this.epochStatistics.epochNewPlants++;
        }
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
            this.epochStatistics.epochEatenPlants++;
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
}
