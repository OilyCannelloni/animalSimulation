package animalSimulation;

import animalSimulation.gui.ImageManager;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public final class Animal extends AbstractMovableElement {
    private static class FacingEnergyPair {
        public Facing facing;
        public int energy;
        public FacingEnergyPair(Facing f, int e) {
            this.energy = e;
            this.facing = f;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.energy, this.facing.ordinal());
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof FacingEnergyPair)) return false;
            FacingEnergyPair that = (FacingEnergyPair) other;
            return this.energy == that.energy && this.facing == that.facing;
        }
    }

    private final int maxEnergy = 30, genomeLength = 32, geneVariants = 8;
    private int energy;
    private final int[] genome;
    private HashMap<FacingEnergyPair, Image> images;
    private ImageManager imageManager;

    public Animal(IWorldMap map, ImageManager imageManager, Vector2d position, int energy) {
        super(map, position);
        this.imageManager = imageManager;
        this.energy = energy;
        this.genome = Algorithm.generateRandomGenome(this.genomeLength, this.geneVariants);
        this.initGraphics();
    }

    public Animal(IWorldMap map, Vector2d position, Animal parent1, Animal parent2) {
        super(map, position);
        this.energy = 10;
        this.genome = new int[this.genomeLength];
        this.initGraphics();
    }

    private void initGraphics() {
        this.images = new HashMap<>();
        for (Facing f : Facing.values()) {
            for (int e = 0; e <= 5; e++) {
                this.images.put(
                        new FacingEnergyPair(f, e),
                        this.imageManager.getImage(String.format("animal_%s_%d.png", f.toString().toLowerCase(), e))
                );
            }
        }
    }

    @Override
    public Image getImage() {
        int mappedEnergy = (int) Algorithm.map(this.energy, 0, this.maxEnergy, 0, 5.999);
        FacingEnergyPair key = new FacingEnergyPair(this.facing, mappedEnergy);
        return this.images.get(new FacingEnergyPair(this.facing, mappedEnergy));
    }

    public int getEnergy(){
        return this.energy;
    }

    public void turn() {
        Random random = new Random();
        int i = random.nextInt(this.genomeLength);
        int delta = this.genome[i];
        this.facing = this.facing.rotate(delta);
    }

    @Override
    protected void onMove(){
        this.energy--;
    }
}

