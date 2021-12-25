package animalSimulation;

import java.util.Arrays;

public class Genome {
    public static int GENOME_LENGTH = 32, VARIANTS = 8;
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
        this.value = intersectGenome(g1, g2, a1.getEnergy(), a2.getEnergy());
    }
    
    public static int[] intersectGenome(int[] g1, int[] g2, double e1, double e2) {
        double prop = e1 / (e1 + e2);
        int a1geneN = (int) Algorithm.map(prop, 0, 1, 0, g1.length);
        boolean fromLeft = Algorithm.random.nextBoolean();
        int[] res = new int[GENOME_LENGTH];

        if (fromLeft) {
            System.arraycopy(g1, 0, res, 0, a1geneN);
            System.arraycopy(g2, a1geneN, res, a1geneN, g1.length - a1geneN);
        } else {
            System.arraycopy(g1, g1.length - a1geneN, res, g1.length - a1geneN, a1geneN);
            System.arraycopy(g2, 0, res, 0, g1.length - a1geneN);
        }

        Arrays.sort(res);
        return res;
    }

    public String toString() {
        return Arrays.toString(this.value);
    }

    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
}
