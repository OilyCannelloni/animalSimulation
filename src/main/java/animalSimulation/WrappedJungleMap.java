package animalSimulation;

public class WrappedJungleMap extends JungleMap {


    public WrappedJungleMap(int width, int height, int junglePercentage,
                            int respawnThreshold, int respawnCopies, int respawnRepeat) {
        super(width, height, junglePercentage, respawnThreshold, respawnCopies, respawnRepeat);
    }

    @Override
    public Vector2d mapTarget(Vector2d target) {
        return new Vector2d(
                target.x % this.boundingBox.getDimensions().x,
                target.y % this.boundingBox.getDimensions().y
        );
    }
}
