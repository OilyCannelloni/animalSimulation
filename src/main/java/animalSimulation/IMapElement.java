package animalSimulation;

public interface IMapElement {
    Vector2d getPosition();

    void interactWith(IMapElement other);

    String getSymbol();

    String toString();
}
