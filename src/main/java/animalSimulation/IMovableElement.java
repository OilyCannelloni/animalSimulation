package animalSimulation;

public interface IMovableElement extends IMapElement {
    void move(boolean forward);
    void addObserver(IActionObserver observer);
    void removeObserver(IActionObserver observer);
    void positionChanged(Vector2d oldPosition, Vector2d newPosition);
}
