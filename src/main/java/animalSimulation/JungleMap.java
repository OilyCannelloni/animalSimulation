package animalSimulation;

public class JungleMap extends AbstractWorldMap {
    private final Rect2D jungleBox;

    public JungleMap(int width, int height, Rect2D jungle) {
        super(width, height);
        this.jungleBox = jungle;
        assert this.boundingBox.contains(this.jungleBox);
    }

    public Rect2D getJungleBox() {
        return this.jungleBox;
    }
}
