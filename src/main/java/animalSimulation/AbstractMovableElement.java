package animalSimulation;

import java.util.LinkedList;

public abstract class AbstractMovableElement extends AbstractMapElement implements IMovableElement {
    protected Facing facing;
    protected LinkedList<IPositionChangeObserver> observers;

    public AbstractMovableElement(IWorldMap map, Vector2d position) {
        super(map, position);
        this.facing = Facing.N;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void move() {
        Vector2d target = this.position.add(this.facing.toUnitVector());
        if (this.map.canMoveTo(target)) {
            this.positionChanged(this.position, target);
            this.position = target;
        }
    }

    @Override
    public void addObserver(IPositionChangeObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(IPositionChangeObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observers) {
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }

    @Override
    public Facing getFacing() {
        return this.facing;
    }
}
