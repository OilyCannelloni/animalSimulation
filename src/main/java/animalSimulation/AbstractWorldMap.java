package animalSimulation;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class AbstractWorldMap implements IWorldMap {
    protected HashMap<Vector2d, LinkedList<IMapElement>> mapElements;
    protected Rect2D boundingBox;

    public AbstractWorldMap(int width, int height) {
        this.mapElements = new HashMap<>();
        this.boundingBox = new Rect2D(
                new Vector2d(0, 0),
                new Vector2d(width - 1, height - 1)
        );
    }

    public Rect2D getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public HashMap<Vector2d, LinkedList<IMapElement>> getElements() {
        return this.mapElements;
    }

    @Override
    public boolean placeElement(IMapElement element) {
        Vector2d position = element.getPosition();
        if (!this.canMoveTo(position)) {
            System.out.printf("Cannot place %s at %s%n", element, position);
            return false;
        }
        LinkedList<IMapElement> elementsAtPosition = this.mapElements.get(position);
        if (elementsAtPosition == null) {
            elementsAtPosition = new LinkedList<>();
            elementsAtPosition.add(element);
            this.mapElements.put(position, elementsAtPosition);
        } else {
            elementsAtPosition.add(element);
        }
        return true;
    }

    private void forcePlaceElement(IMapElement element) {
        Vector2d position = element.getPosition();
        LinkedList<IMapElement> elementsAtPosition = this.mapElements.get(position);
        if (elementsAtPosition == null) {
            elementsAtPosition = new LinkedList<>();
            elementsAtPosition.add(element);
            this.mapElements.put(position, elementsAtPosition);
        } else {
            elementsAtPosition.add(element);
        }
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
        return true;
    }

    @Override
    public LinkedList<IMapElement> ElementsAt(Vector2d position) {
        return this.mapElements.get(position);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position) && this.boundingBox.contains(position);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        if (this.ElementsAt(position) == null) return false;
        return !this.ElementsAt(position).isEmpty();
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        assert this.removeElement(element);
        this.forcePlaceElement(element);
    }
}
