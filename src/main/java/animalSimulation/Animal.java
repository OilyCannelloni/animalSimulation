package animalSimulation;

public final class Animal extends AbstractMovableElement {
    private final int energy;
    private final int[] genome;

    public Animal(IWorldMap map, Vector2d position, int energy) {
        super(map, position);
        this.energy = energy;
        this.genome = new int[32];
    }

    public Animal(IWorldMap map, Vector2d position, Animal parent1, Animal parent2) {
        super(map, position);
        this.energy = 10;
        this.genome = new int[32];
    }
}

