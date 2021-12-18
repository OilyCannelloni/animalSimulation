package animalSimulation;

import java.util.Arrays;

public class EpochStatistics {
    public int
            epochAnimalCount = 0,
            epochEatenPlants = 0,
            epochNewPlants,
            epochTotalEnergy = 0,
            epochDeadCount = 0,
            epochTotalDeadLifespan = 0,
            epochTotalChildCount,
            epochBornCount = 0,
            epochDominantGenomeCount = 0;

    public float
            epochAvgChildCount,
            epochAvgEnergy = 0;

    public int[] epochDominantGenome = new int[32];

    public EpochStatistics() {
        Arrays.fill(this.epochDominantGenome, 0);
    }

    public void update(int[] epochDominantGenome) {
        this.epochAvgEnergy = ((float) this.epochTotalEnergy) / this.epochAnimalCount;
        this.epochAvgChildCount = ((float) this.epochTotalChildCount) / this.epochAnimalCount;
        this.epochDominantGenome = epochDominantGenome;
    }
}
