package animalSimulation;

import java.util.LinkedList;

public class SimulationStatistics {
    public LinkedList<EpochStatistics> simulationLog;

    public long
            allTimeDeadCount = 1,
            totalDeadLifespan = 1,
            allTimeBornCount = 1,
            currentAliveAnimals = 0,
            currentAlivePlants = 0,
            currentDominantGenomeCount = 0;


    public float
            allTimeAvgLifespan = 1,
            currentAverageChildCount = 0,
            currentAverageEnergy = 0;

    public int[] currentDominantGenome = new int[32];

    public SimulationStatistics() {
        this.simulationLog = new LinkedList<>();
    }

    public void update(EpochStatistics epoch) {
        this.simulationLog.add(epoch);

        this.totalDeadLifespan += epoch.epochTotalDeadLifespan;
        this.allTimeDeadCount += epoch.epochDeadCount;
        this.allTimeAvgLifespan = ((float) this.totalDeadLifespan) / this.allTimeDeadCount;
        this.allTimeBornCount += epoch.epochBornCount;
        this.currentAverageChildCount = epoch.epochAvgChildCount;
        this.currentAliveAnimals = epoch.epochAnimalCount;
        this.currentAlivePlants += epoch.epochNewPlants - epoch.epochEatenPlants;
        this.currentAverageEnergy = epoch.epochAvgEnergy;
        this.currentDominantGenome = epoch.epochDominantGenome;
        this.currentDominantGenomeCount = epoch.epochDominantGenomeCount;
    }
}
