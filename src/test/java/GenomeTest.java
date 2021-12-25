import animalSimulation.Genome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenomeTest {

    @Test
    public void hashCodeTest() {
        Genome g1 = new Genome();
        Genome g2 = new Genome();
        System.arraycopy(g1.value, 0, g2.value, 0, g2.value.length);
        Assertions.assertEquals(g1.hashCode(), g2.hashCode());
    }
}
