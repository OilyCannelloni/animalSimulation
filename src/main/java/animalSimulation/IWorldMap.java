package animalSimulation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public interface IWorldMap extends IActionObserver {
    void placeElement(IMapElement element);

    boolean removeElement(IMapElement element);

    LinkedList<IMapElement> ElementsAt(Vector2d position);

    boolean canMoveTo(Vector2d position);

    boolean isOccupied(Vector2d position);

    Rect2D getBoundingBox();

    HashMap<Vector2d, LinkedList<IMapElement>> getElements();

    LinkedList<IMovableElement> getMovableElements();

    void updateField(Vector2d position);

    HashSet<Vector2d> getUpdatedFields();

    void clearUpdatedFields();

    void removeAllElementsOfType(Class<?> cls);

    Vector2d mapTarget(Vector2d target);
}
