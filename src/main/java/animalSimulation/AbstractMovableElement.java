package animalSimulation;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMovableElement extends AbstractMapElement implements IMovableElement {
    protected Facing facing;
    protected LinkedList<IActionObserver> observers;

    public AbstractMovableElement(IWorldMap map, Vector2d position, List<IActionObserver> observers) {
        super(map, position);
        this.facing = Facing.getRandom();
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
        Vector2d expected_target = this.position.add(this.facing.toUnitVector().multiplyEach(mul));
        Vector2d target = this.map.mapTarget(expected_target);
        if (this.map.canMoveTo(target)) {
            this.onMove();
            this.positionChanged(this.position, target);
            this.position = target;

            if (!target.equals(expected_target)) System.out.println("Wrapped at " + target);
        }
    }

    @Override
    public void addObserver(IActionObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(IActionObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IActionObserver observer : this.observers) {
            observer.positionChanged(this, oldPosition, newPosition);
        }
    }

    @Override
    public Facing getFacing() {
        return this.facing;
    }

    protected void onMove() {}
}
