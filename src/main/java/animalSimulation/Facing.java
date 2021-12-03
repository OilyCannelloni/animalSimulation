package animalSimulation;

public enum Facing {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;

    public Facing rotate(int steps) {
        return Facing.values()[(this.ordinal() + steps) % 8];
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case N: return new Vector2d(0, 1);
            case NE: return new Vector2d(1, 1);
            case E: return new Vector2d(1, 0);
            case SE: return new Vector2d(1, -1);
            case S: return new Vector2d(0, -1);
            case SW: return new Vector2d(-1, -1);
            case W: return new Vector2d(-1, 0);
            case NW: return new Vector2d(-1, 1);
            default: return new Vector2d(0, 0);
        }
    }
}
