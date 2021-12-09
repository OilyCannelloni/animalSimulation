package animalSimulation;

import animalSimulation.gui.ImageManager;

public class PlantFactory extends ElementFactory implements IFactory<Plant> {
    private final boolean isJunglePlant;
    private final int plantEnergy;

    public PlantFactory(IWorldMap map, ImageManager imageManager, boolean isJunglePlant, int plantEnergy) {
        super(map, imageManager);
        this.isJunglePlant = isJunglePlant;
        this.plantEnergy = plantEnergy;
    }

    @Override
    public Plant create(Vector2d position) {
        Plant p = new Plant(
                this.map,
                this.imageManager,
                position,
                this.plantEnergy
        );
        p.isJunglePlant = this.isJunglePlant;
        return p;
    }

    @Override
    public void createPlace(Vector2d position) {
        this.map.placeElement(this.create(position));
    }
}
