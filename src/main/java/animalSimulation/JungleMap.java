package animalSimulation;

import animalSimulation.gui.ImageManager;

public class JungleMap extends AbstractWorldMap {
    private final Rect2D jungleBox;
    private final int respawnThreshold;
    private int respawnCopies;
    private int respawnRepeat, initAnimals;
    public AnimalFactory animalFactory;
    public PlantFactory plantFactory, junglePlantFactory;

    public JungleMap(ImageManager imageManager,
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
        super(width, height);


        this.animalFactory = new AnimalFactory(this, imageManager, startEnergy, moveEnergy);
        this.plantFactory = new PlantFactory(this, imageManager, false, plantEnergy);
        this.junglePlantFactory = new PlantFactory(this, imageManager, true, plantEnergy);

        this.initAnimals = initAnimals;
        this.respawnThreshold = respawnThreshold;
        this.respawnCopies = respawnCopies;
        this.respawnRepeat = respawnRepeat;

        this.jungleBox = this.createJungleBox(junglePercentage);

        System.out.println(this.jungleBox);
        assert this.boundingBox.contains(this.jungleBox);
    }

    public void initialize() {
        for (int i = 0; i < this.initAnimals; i++) {
            Vector2d position = Algorithm.getRandomEmptyFieldOutside(this, this.getJungleBox());
            this.animalFactory.createPlace(position);
        }
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
        int movableCount = this.movableElements.size();
        if (movableCount > this.respawnThreshold) return;
        if (--this.respawnRepeat < 0) return;

        for (IMovableElement movableElement : this.movableElements) {
            if (movableElement instanceof Animal) {
                Animal animal = (Animal) movableElement;
                for (int i = 0; i < this.respawnCopies; i++)
                    this.animalFactory.copy(animal, Algorithm.getRandomEmptyField(this));
            }
        }

    }
}
