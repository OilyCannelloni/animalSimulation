package animalSimulation;

import animalSimulation.gui.ImageManager;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    public final static int maxEnergy = 30, genomeLength = 32, geneVariants = 8;
    private int energy;
    private final int moveEnergy, startEnergy;
    private final int[] genome;
    private HashMap<FacingEnergyPair, Image> images;
    private final ImageManager imageManager;

    public Animal(
            IWorldMap map,
            ImageManager imageManager,
            Vector2d position,
            int startEnergy,
            int energy,
            int moveEnergy,
            List<IPositionChangeObserver> observers,
            int[] genome
    ) {
        super(map, position, observers);
        this.imageManager = imageManager;
        this.energy = energy;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.genome = genome;
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
        int mappedEnergy = (int) Algorithm.map(this.energy, 0, this.startEnergy, 0, 5.999);
        FacingEnergyPair key = new FacingEnergyPair(this.facing, mappedEnergy);
        return this.images.get(new FacingEnergyPair(this.facing, mappedEnergy));
    }

    public int getEnergy(){
        return this.energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
        if (this.energy > this.startEnergy) this.energy = startEnergy;
    }

    public int[] getGenome() {
        return this.genome;
    }

    public void makeMove() {
        int delta = Algorithm.getRandom(this.genome);
        if (delta == 0) this.move(true);
        else if (delta == 4) this.move(false);
        else {
            this.facing = this.facing.rotate(delta);
            this.map.updateField(this.position);
        }
        this.energy -= this.moveEnergy;
    }
}

