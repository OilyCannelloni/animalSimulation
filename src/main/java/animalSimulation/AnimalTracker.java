package animalSimulation;

import java.util.HashSet;

public class AnimalTracker extends ActionObserver {
    private Animal animal;
    private final IWorldMap map;
    private int totalChildren = 0;
    private final HashSet<Animal> descendants;
    private TrackingCircle trackingCircle;

    public AnimalTracker(Animal animal) {
        this.animal = animal;
        this.map = animal.map;
        this.descendants = new HashSet<>();
        this.descendants.add(animal);
        this.animal.addObserver(this);
        this.trackingCircle = new TrackingCircle(this.map, animal.getPosition());
        this.map.placeElement(this.trackingCircle);
    }

    public void setAnimal(Animal animal) {
        this.animal.removeObserver(this);
        this.map.removeElement(this.trackingCircle);
        this.animal = animal;
        this.descendants.clear();
        this.descendants.add(animal);
        this.animal.addObserver(this);
        this.trackingCircle = new TrackingCircle(this.map, animal.getPosition());
        this.map.placeElement(this.trackingCircle);
    }

    @Override
    public void animalBorn(Animal parent1, Animal parent2, Animal child) {
        if (parent1 == this.animal || parent2 == this.animal)
            this.totalChildren++;
        if (this.descendants.contains(parent1) || this.descendants.contains(parent2)) {
            this.descendants.add(child);
        }
    }

    @Override
    public void animalDied(Animal animal) {
        this.descendants.remove(animal);
        if (animal == this.animal) {
            this.animal.removeObserver(this);
            this.map.removeElement(this.trackingCircle);
        }
    }

    public int getDescendantCount() {
        int s = this.descendants.size();
        return s == 0 ? 0 : s - 1;
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        if (element instanceof Animal) {
            Animal a = (Animal) element;
            if (a == this.animal) {
                map.removeElement(this.trackingCircle);
                this.trackingCircle.position = newPosition;
                map.placeElement(this.trackingCircle);
            }
        }
    }

    public AnimalStatistics getAnimalStatistics() {
        if (this.animal == null) return new AnimalStatistics();

        AnimalStatistics stats = new AnimalStatistics();
        stats.aliveDescendantCount = this.getDescendantCount();
        stats.diedEpoch = this.animal.deathEpoch;
        stats.childCount = this.totalChildren;
        stats.genome = this.animal.getGenome();
        stats.position = this.animal.getPosition();
        stats.energy = this.animal.getEnergy();
        return stats;
    }
}
