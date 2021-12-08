package animalSimulation;

import animalSimulation.gui.ImageManager;

import java.util.List;

public class AnimalFactory extends MovableElementFactory implements IFactory<Animal> {

    public AnimalFactory(
            IWorldMap map,
            ImageManager imageManager,
            List<IPositionChangeObserver> observers
    ) {
        super(map, imageManager, observers);
    }

    @Override
    public Animal create(Vector2d position) {
        return new Animal(
                this.map,
                this.imageManager,
                position,
                Animal.maxEnergy,
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
                this.observers,
                Algorithm.intersectGenome(parent1.getGenome(), parent2.getGenome())
        );
    }
}
