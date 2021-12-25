package animalSimulation;

import java.util.Arrays;
import java.util.Objects;

public class Genome {
    public int GENOME_LENGTH = 32, VARIANTS = 8;
    public int[] value;

    public static Genome empty() {
        Genome g = new Genome();
        Arrays.fill(g.value, 0);
        return g;
    }

    public Genome() {
        this.value = new int[GENOME_LENGTH];
        for (int i = 0; i < GENOME_LENGTH; i++) {
            this.value[i] = Algorithm.random.nextInt(VARIANTS);
            Arrays.sort(this.value);
        }
    }

    public Genome(Animal a1, Animal a2) {
        int[] g1 = a1.getGenome().value, g2 = a2.getGenome().value;
        double e1 = a1.getEnergy(), e2 = a2.getEnergy();
        double prop = e1 / (e1 + e2);
        int a1geneN = (int) Algorithm.map(prop, 0, 1, 0, g1.length);
        boolean fromLeft = Algorithm.random.nextBoolean();
        this.value = new int[GENOME_LENGTH];

        if (fromLeft) {
            System.arraycopy(g1, 0, this.value, 0, a1geneN);
            System.arraycopy(g2, a1geneN, this.value, a1geneN, g1.length - a1geneN);
        } else {
            System.arraycopy(g1, g1.length - a1geneN, this.value, g1.length - a1geneN, a1geneN);
            System.arraycopy(g2, 0, this.value, 0, g1.length - a1geneN);
        }

        Arrays.sort(this.value);
    }

    public String toString() {
        return Arrays.toString(this.value);
    }

    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
}
