package animalSimulation;

public class JungleMap extends AbstractWorldMap {
    private final Rect2D jungleBox;
    private int respawnThreshold, respawnCopies, respawnRepeat;

    public JungleMap(int width, int height, int junglePercentage,
                     int respawnThreshold, int respawnCopies, int respawnRepeat) {
        super(width, height);

        this.respawnThreshold = respawnThreshold;
        this.respawnCopies = respawnCopies;
        this.respawnRepeat = respawnRepeat;

        this.jungleBox = this.createJungleBox(junglePercentage);

        System.out.println(this.jungleBox);
        assert this.boundingBox.contains(this.jungleBox);


    }

    private Rect2D createJungleBox(int junglePercentage) {
        double sideScaleFactor = Math.sqrt(((double) junglePercentage) / 100);
        int height = this.getBoundingBox().getDimensions().y, width = this.getBoundingBox().getDimensions().x;
        int scaledWidth = (int) (width * sideScaleFactor);
        int scaledHeight = (int) (height * sideScaleFactor);
        return new Rect2D(
                new Vector2d(
                        (width - scaledWidth) / 2,
                        (height - scaledHeight) / 2

                ),
                new Vector2d(
                        (width + scaledWidth) / 2,
                        (height + scaledHeight) / 2
                )
        );
    }

    public Rect2D getJungleBox() {
        return this.jungleBox;
    }

    public void respawn() {
        if (--this.respawnRepeat < 0) return;


    }
}
