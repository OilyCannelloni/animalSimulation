package animalSimulation;

import java.util.HashSet;

public class AnimalTracker extends ActionObserver {
    private final Animal animal;
    private final IWorldMap map;
    private int totalChildren = 0;
    private final HashSet<Animal> descendants;

    public AnimalTracker(Animal animal) {
        this.animal = animal;
        this.map = animal.map;
        this.descendants = new HashSet<>();
        this.descendants.add(animal);
        this.animal.addObserver(this);
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
    }

    public int getDescendantCount() {
        int s = this.descendants.size();
        return s == 0 ? 0 : s - 1;
    }

    public AnimalStatistics getAnimalStatistics() {
        if (this.animal == null) return new AnimalStatistics();

        AnimalStatistics stats = new AnimalStatistics();
        stats.aliveDescendantCount = this.getDescendantCount();
        stats.diedEpoch = this.animal.deathEpoch;
        stats.childCount = this.totalChildren;
        stats.genome = this.animal.getGenome();
        stats.position = this.animal.getPosition();
        return stats;
    }
}
