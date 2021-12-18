package animalSimulation;

public interface IActionObserver {
    void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition);

    void animalBorn(Animal parent1, Animal parent2, Animal child);

    void animalDied(Animal animal);
}