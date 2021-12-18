package animalSimulation;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;

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

        this.totalDeadLifespan += epoch.epochDeadLifespan;
        this.allTimeDeadCount += epoch.epochDeadCount;
        this.allTimeAvgLifespan = ((float) this.totalDeadLifespan) / this.allTimeDeadCount;
        this.allTimeBornCount += epoch.epochBornCount;
        this.currentAverageChildCount = epoch.epochAvgChildCount;
        this.currentAliveAnimals = epoch.epochAnimalCount;
        this.currentAlivePlants += epoch.epochNewPlants - epoch.epochEatenPlants;
        this.currentAverageEnergy = epoch.epochAvgEnergy;
        this.currentDominantGenome = epoch.epochDominantGenome;
        this.currentDominantGenomeCount = epoch.epochDominantGenomeCount;

        epoch.epochTotalDeadLifespan = this.totalDeadLifespan + epoch.epochDeadLifespan;
        epoch.epochAvgTotalDeadLifespan = ((float) epoch.epochTotalDeadLifespan) / this.allTimeDeadCount;
    }

    public void saveToCSV(String prefix) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
        String fileName = prefix + format.format(date);
        CSVWriter csvWriter = new CSVWriter(".\\saves\\" + fileName + ".csv");

        LinkedList<String> columnNames = new LinkedList<>();
        columnNames.add("EpochID");

        Field[] fields = EpochStatistics.class.getFields();
        for (Field f : fields) {
            if (f.getType() == Float.TYPE || f.getType() == Integer.TYPE || f.getType() == Long.TYPE) {
                columnNames.add(f.getName());
            }
        }

        csvWriter.writeHeader(columnNames);

        ListIterator<EpochStatistics> iterator = simulationLog.listIterator();
        while (iterator.hasNext()) {
            LinkedList<Number> entry = new LinkedList<>();
            entry.add(iterator.nextIndex() + 1);

            EpochStatistics statistics = iterator.next();
            for (String fieldName : columnNames) {
                try {
                    Field field = EpochStatistics.class.getField(fieldName);
                    entry.add((Number) field.get(statistics));
                } catch (NoSuchFieldException | IllegalAccessException ignore) {}
            }

            csvWriter.writeEntry(entry);
        }

        LinkedList<Number> avgEntry = new LinkedList<>();
        avgEntry.add(-1);
        for (String fieldName : columnNames) {
            try {
                Field field = EpochStatistics.class.getField(fieldName);
                double value = 0;
                for (EpochStatistics statistics : this.simulationLog) {
                    value += ((Number) field.get(statistics)).doubleValue();
                }
                avgEntry.add(value / this.simulationLog.size());
            } catch (NoSuchFieldException | IllegalAccessException ignore) {}
        }

        csvWriter.writeEntry(avgEntry);

        csvWriter.close();
        System.out.println("Saved statistics as " + fileName);
    }
}
