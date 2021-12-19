package animalSimulation;

import animalSimulation.gui.ImageManager;

public class WrappedJungleMap extends JungleMap {


    public WrappedJungleMap(ImageManager imageManager,
                            int startEnergy,
                            int moveEnergy,
                            int initAnimals,
                            int plantEnergy,
                            int width,
                            int height,
                            int junglePercentage,
                            int respawnThreshold,
                            int respawnCopies,
                            int respawnRepeat
    ) {
        super(imageManager, startEnergy, moveEnergy, initAnimals, plantEnergy, width, height,
                junglePercentage, respawnThreshold, respawnCopies, respawnRepeat);
    }

    @Override
    public Vector2d mapTarget(Vector2d target) {
        return new Vector2d(
                target.x % this.boundingBox.getDimensions().x,
                target.y % this.boundingBox.getDimensions().y
        );
    }
}
