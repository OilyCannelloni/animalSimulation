package animalSimulation;

public abstract class AbstractMapElement implements IMapElement {
    protected Vector2d position;
    protected IWorldMap map;

    public AbstractMapElement(IWorldMap map, Vector2d position) {
        this.position = position;
        this.map = map;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public void interactWith(IMapElement other) {

    }
}
