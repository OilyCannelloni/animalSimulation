package animalSimulation;

import javax.swing.*;
import java.util.Random;

public final class Animal extends AbstractMovableElement {
    private final int energy, genomeLength = 32, geneVariants = 8;
    private final int[] genome;
    private final ImageIcon icon = new ImageIcon(".\\src\\main\\resources\\images\\animal_n_5.png");

    public Animal(IWorldMap map, Vector2d position, int energy) {
        super(map, position);
        this.energy = energy;
        this.genome = Algorithm.generateRandomGenome(this.genomeLength, this.geneVariants);
    }

    public Animal(IWorldMap map, Vector2d position, Animal parent1, Animal parent2) {
        super(map, position);
        this.energy = 10;
        this.genome = new int[this.genomeLength];
    }

    @Override
    public ImageIcon getIcon() {
        return this.icon;
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
}

