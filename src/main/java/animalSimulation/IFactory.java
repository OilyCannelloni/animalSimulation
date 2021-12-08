package animalSimulation;

public interface IFactory<T> {
    T create(Vector2d position);
    void createPlace(Vector2d position);
}
