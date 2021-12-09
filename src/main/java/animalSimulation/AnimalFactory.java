package animalSimulation;

import animalSimulation.gui.ImageManager;

import java.util.LinkedList;
import java.util.List;

public class AnimalFactory extends MovableElementFactory implements IFactory<Animal> {

    private final int startEnergy, moveEnergy;
    public AnimalFactory(
            IWorldMap map,
            ImageManager imageManager,
            List<IPositionChangeObserver> observers,
            int startEnergy,
            int moveEnergy
    ) {
        super(map, imageManager, observers);
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
    }

    public AnimalFactory(
            IWorldMap map,
            ImageManager imageManager,
            int startEnergy,
            int moveEnergy
    ) {
        this(map, imageManager, new LinkedList<IPositionChangeObserver>(), startEnergy, moveEnergy);
    }

    @Override
    public Animal create(Vector2d position) {
        return new Animal(
                this.map,
                this.imageManager,
                position,
                this.startEnergy,
                this.moveEnergy,
                this.observers,
                Algorithm.generateRandomGenome(Animal.genomeLength, Animal.geneVariants)
        );
    }

    @Override
    public void createPlace(Vector2d position) {
        this.map.placeElement(this.create(position));
    }

    public Animal create(Animal parent1, Animal parent2) {
        return new Animal(
                this.map,
                this.imageManager,
                parent1.getPosition(),
                Animal.maxEnergy * 2 / 3,
                this.moveEnergy,
                this.observers,
                Algorithm.intersectGenome(parent1.getGenome(), parent2.getGenome())
        );
    }
}
