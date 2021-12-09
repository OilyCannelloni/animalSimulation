package animalSimulation;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMovableElement extends AbstractMapElement implements IMovableElement {
    protected Facing facing;
    protected LinkedList<IPositionChangeObserver> observers;

    public AbstractMovableElement(IWorldMap map, Vector2d position, List<IPositionChangeObserver> observers) {
        super(map, position);
        this.facing = Facing.N;
        this.observers = new LinkedList<>();
        this.addObserver(map);
        this.observers.addAll(observers);
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void move(boolean forward) {
        int mul = forward ? 1 : -1;
        Vector2d target = this.position.add(this.facing.toUnitVector().multiplyEach(mul));
        if (this.map.canMoveTo(target)) {
            this.onMove();
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

    protected void onMove() {}
}
