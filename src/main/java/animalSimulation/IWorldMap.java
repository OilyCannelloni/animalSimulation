package animalSimulation;

import java.util.HashMap;
import java.util.LinkedList;

public interface IWorldMap extends IPositionChangeObserver {
    boolean placeElement(IMapElement element);

    boolean removeElement(IMapElement element);

    LinkedList<IMapElement> ElementsAt(Vector2d position);

    boolean canMoveTo(Vector2d position);

    boolean isOccupied(Vector2d position);

    Rect2D getBoundingBox();

    HashMap<Vector2d, LinkedList<IMapElement>> getElements();
}
