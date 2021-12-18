package animalSimulation;

public abstract class ActionObserver implements IActionObserver {
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {}
    public void animalBorn(Animal parent1, Animal parent2, Animal child) {}
    public void animalDied(Animal animal) {}
}
