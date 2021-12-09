package animalSimulation;

import animalSimulation.gui.ImageManager;
import javafx.scene.image.Image;

public class Plant extends AbstractMapElement {
    private final int plantEnergy;
    private final ImageManager imageManager;
    public boolean isJunglePlant = false;

    public Plant(IWorldMap map, ImageManager imageManager, Vector2d position, int plantEnergy) {
        super(map, position);
        this.plantEnergy = plantEnergy;
        this.imageManager = imageManager;
    }

    public int getPlantEnergy() {
        return this.plantEnergy;
    }

    @Override
    public Image getImage() {
        if (this.isJunglePlant) {
            return this.imageManager.getImage("plant_2.png");
        }
        return this.imageManager.getImage("plant_1.png");
    }
}
