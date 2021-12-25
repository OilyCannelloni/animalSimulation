import animalSimulation.Animal;
import animalSimulation.Genome;
import animalSimulation.IWorldMap;
import animalSimulation.JungleMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class GenomeTest {

    @Test
    public void hashCodeTest() {
        Genome g1 = new Genome();
        Genome g2 = new Genome();
        System.arraycopy(g1.value, 0, g2.value, 0, g2.value.length);
        Assertions.assertEquals(g1.hashCode(), g2.hashCode());
    }

    @Test
    public void genomeTest() {
        int[] g1 = new int[32];
        g1[31] = 1;
        g1[30] = 1;
        int[] g2 = new int[32];
        Arrays.fill(g2, 7);

        int[] res = Genome.intersectGenome(g1, g2, 30, 100);
        System.out.println(Arrays.toString(res));
    }
}
