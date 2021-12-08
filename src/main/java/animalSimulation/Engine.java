package animalSimulation;

import java.util.HashSet;

public class Engine implements IPositionChangeObserver {
    private final IWorldMap map;
    public final HashSet<Vector2d> changedPositions;

    public Engine(IWorldMap map) {
        this.map = map;
        this.changedPositions = new HashSet<>();
    }

    public void processTurn() {
        // remove dead bodies
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                if (a.getEnergy() <= 0) map.removeElement(a);
            }
        }

        // turn and move animals
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                a.turn();
                a.move();
            }
        }
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        this.changedPositions.add(oldPosition);
        this.changedPositions.add(newPosition);
    }

    public void endTurn() {
        this.changedPositions.clear();
    }
}
