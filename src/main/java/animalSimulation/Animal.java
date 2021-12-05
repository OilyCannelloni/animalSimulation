package animalSimulation;

import javax.swing.*;
import java.util.Random;

public final class Animal extends AbstractMovableElement {
    private final int maxEnergy = 30, genomeLength = 32, geneVariants = 8;
    private int energy;
    private final int[] genome;
    private final IconManager icons;

    public Animal(IWorldMap map, IconManager icons, Vector2d position, int energy) {
        super(map, position);
        this.icons = icons;
        this.energy = energy;
        this.genome = Algorithm.generateRandomGenome(this.genomeLength, this.geneVariants);
    }

    public Animal(IWorldMap map, IconManager icons, Vector2d position, Animal parent1, Animal parent2) {
        super(map, position);
        this.energy = 10;
        this.genome = new int[this.genomeLength];
        this.icons = icons;
    }

    @Override
    public ImageIcon getIcon() {
        int mappedEnergy = (int) Algorithm.map(this.energy, 0, this.maxEnergy, 0, 5.999);
        return this.icons.getAnimalIcon(this.facing, mappedEnergy);
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

