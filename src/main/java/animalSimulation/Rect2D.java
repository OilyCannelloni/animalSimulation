package animalSimulation;

public class Rect2D {
    public Vector2d lowerLeft;
    public Vector2d upperRight;

    public Rect2D(Vector2d ll, Vector2d ur){
        this.lowerLeft = ll;
        this.upperRight = ur;
    }

    public boolean contains(Vector2d point) {
        return point.follows(this.lowerLeft) && point.precedes(this.upperRight);
    }

    public Vector2d getDimensions() {
        return new Vector2d(upperRight.x - lowerLeft.x + 1, upperRight.y - lowerLeft.y + 1);
    }

    public boolean equals(Object other){
        if (this == other) return true;
        if (! (other instanceof Rect2D)) return false;
        Rect2D that = (Rect2D) other;
        return this.lowerLeft.equals(that.lowerLeft) && this.upperRight.equals(that.upperRight);
    }

    public String toString() {
        return this.lowerLeft.toString() + " " + this.upperRight.toString();
    }
}
