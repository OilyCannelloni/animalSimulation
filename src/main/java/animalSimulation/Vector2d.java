package animalSimulation;

import java.util.Objects;


public class Vector2d {
    public final int x, y;
    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2d(int linear, Rect2D area) {
        Vector2d dimensions = area.getDimensions();
        this.x = linear % dimensions.x;
        this.y = linear / dimensions.x;
    }

    public String toString(){
        return String.format("(%d,%d)", this.x, this.y);
    }

    public boolean precedes(Vector2d other){
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other){
        return this.x >= other.x && this.y >= other.y;
    }

    public Vector2d upperRight(Vector2d other){
        return new Vector2d(
                Math.max(this.x, other.x),
                Math.max(this.y, other.y)
        );
    }

    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d(
                Math.min(this.x, other.x),
                Math.min(this.y, other.y)
        );
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other){
        if (this == other) return true;
        if (! (other instanceof Vector2d)) return false;
        Vector2d that = (Vector2d) other;
        return this.x == that.x && this.y == that.y;
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

    public int toLinear(Rect2D area) {
        assert area.contains(this);
        Vector2d dimensions = area.getDimensions();
        Vector2d relative = this.subtract(area.lowerLeft);
        return dimensions.x * (relative.y) + relative.x;
    }

    public Vector2d multiplyEach(Vector2d other) {
        return new Vector2d(this.x * other.x, this.y * other.y);
    }

    public Vector2d multiplyEach(int a) {
        return new Vector2d(
                this.x * a,
                this.y * a
        );
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
