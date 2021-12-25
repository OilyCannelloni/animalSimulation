package animalSimulation;

import animalSimulation.gui.ImageManager;

import java.util.LinkedList;
import java.util.List;

public class AnimalFactory extends MovableElementFactory implements IFactory<Animal> {

    public final int startEnergy, moveEnergy;
    public LinkedList<IActionObserver> factoryObservers;

    public AnimalFactory(
            IWorldMap map,
            ImageManager imageManager,
            List<IActionObserver> observers,
            int startEnergy,
            int moveEnergy
    ) {
        super(map, imageManager, observers);
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.factoryObservers = new LinkedList<>();
    }

    public AnimalFactory(
            IWorldMap map,
            ImageManager imageManager,
            int startEnergy,
            int moveEnergy
    ) {
        this(map, imageManager, new LinkedList<>(), startEnergy, moveEnergy);
    }

    @Override
    public Animal create(Vector2d position) {
        return new Animal(
                this.map,
                this.imageManager,
                position,
                this.startEnergy,
                this.startEnergy,
                this.moveEnergy,
                this.observers,
                new Genome()
        );
    }

    @Override
    public void createPlace(Vector2d position) {
        Animal a = this.create(position);
        this.map.placeElement(a);
    }

    public void kill(Animal animal, int epoch) {
        animal.deathEpoch = epoch;
        this.map.removeElement(animal);
        for (IActionObserver observer : this.factoryObservers) observer.animalDied(animal);
    }

    public Animal copy(Animal animal, Vector2d position) {
        return new Animal(
                this.map,
                this.imageManager,
                position,
                this.startEnergy,
                this.startEnergy,
                this.moveEnergy,
                this.observers,
                animal.getGenome()
        );
    }

    public Animal create(Animal parent1, Animal parent2) {
        int energyTransfer1 = parent1.getEnergy() / 4, energyTransfer2 = parent2.getEnergy() / 4;
        parent1.addEnergy(-energyTransfer1);
        parent2.addEnergy(-energyTransfer2);
        parent1.childCount++;
        parent2.childCount++;

        Animal a = new Animal(
                this.map,
                this.imageManager,
                parent1.getPosition(),
                this.startEnergy,
                energyTransfer1 + energyTransfer2,
                this.moveEnergy,
                this.observers,
                new Genome(parent1, parent2)
        );

        for (IActionObserver observer : this.factoryObservers) observer.animalBorn(parent1, parent2, a);
        return a;
    }
}
