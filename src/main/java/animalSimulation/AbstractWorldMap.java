package animalSimulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public abstract class AbstractWorldMap implements IWorldMap {
    protected HashMap<Vector2d, LinkedList<IMapElement>> mapElements;
    protected LinkedList<IMovableElement> movableElements;
    protected Rect2D boundingBox;
    protected HashSet<Vector2d> updatedFields;
    public String name = "map";

    public AbstractWorldMap(int width, int height) {
        this.mapElements = new HashMap<>();
        this.movableElements = new LinkedList<>();
        this.boundingBox = new Rect2D(
                new Vector2d(0, 0),
                new Vector2d(width - 1, height - 1)
        );
        this.updatedFields = new HashSet<>();
    }

    public Rect2D getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public HashMap<Vector2d, LinkedList<IMapElement>> getElements() {
        return this.mapElements;
    }

    @Override
    public LinkedList<IMovableElement> getMovableElements() {
        return new LinkedList<>(this.movableElements);
    }

    @Override
    public void placeElement(IMapElement element) {
        Vector2d position = element.getPosition();
        if (!this.canMoveTo(position)) {
            System.out.printf("Cannot place %s at %s%n", element, position);
            return;
        }
        LinkedList<IMapElement> elementsAtPosition = this.mapElements.get(position);
        if (elementsAtPosition == null) {
            elementsAtPosition = new LinkedList<>();
            elementsAtPosition.add(element);
            this.mapElements.put(position, elementsAtPosition);
        } else {
            elementsAtPosition.add(element);
        }

        if (element instanceof IMovableElement) {
            this.movableElements.add((IMovableElement) element);
        }

        this.updatedFields.add(position);
    }

    protected void forcePlaceElement(IMapElement element, Vector2d position) {
        LinkedList<IMapElement> elementsAtPosition = this.mapElements.get(position);
        if (elementsAtPosition == null) {
            elementsAtPosition = new LinkedList<>();
            elementsAtPosition.add(element);
            this.mapElements.put(position, elementsAtPosition);
        } else {
            elementsAtPosition.add(element);
        }

        if (element instanceof IMovableElement) {
            this.movableElements.add((IMovableElement) element);
        }

        this.updatedFields.add(position);
    }

    @Override
    public boolean removeElement(IMapElement element) {
        Vector2d position = element.getPosition();
        LinkedList<IMapElement> elementsAtPosition = this.mapElements.get(position);
        if (elementsAtPosition == null || !elementsAtPosition.remove(element)) {
            System.out.printf("Cannot remove %s from %s%n", element, position);
            return false;
        }
        if (elementsAtPosition.isEmpty()) this.mapElements.remove(position);

        if (element instanceof IMovableElement) {
            this.movableElements.remove((IMovableElement) element);
        }
        this.updatedFields.add(position);
        return true;
    }

    @Override
    public LinkedList<IMapElement> ElementsAt(Vector2d position) {
        return this.mapElements.get(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return this.boundingBox.contains(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (this.ElementsAt(position) == null) return false;
        return !this.ElementsAt(position).isEmpty();
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        if (oldPosition.equals(newPosition)) {
            this.updatedFields.add(oldPosition);
            return;
        }

        boolean wasRemoved = this.removeElement(element);
        assert wasRemoved;
        this.forcePlaceElement(element, newPosition);
    }

    @Override
    public void animalBorn(Animal parent1, Animal parent2, Animal child) {}

    @Override
    public void animalDied(Animal animal) {}

    @Override
    public void updateField(Vector2d position) {
        this.updatedFields.add(position);
    }

    @Override
    public HashSet<Vector2d> getUpdatedFields() {
        return this.updatedFields;
    }

    @Override
    public void clearUpdatedFields() {
        this.updatedFields.clear();
    }

    @Override
    public void removeAllElementsOfType(Class<?> cls) {
        LinkedList<IMapElement> toRemove = new LinkedList<>();
        for (LinkedList<IMapElement> elements : this.getElements().values()) {
            for (IMapElement element : elements) {
                if (element.getClass() == cls) {
                    toRemove.add(element);
                }
            }
        }
        for (IMapElement element : toRemove) {
            this.removeElement(element);
        }
    }

    @Override
    public Vector2d mapTarget(Vector2d target) {
        return target;
    }
}
