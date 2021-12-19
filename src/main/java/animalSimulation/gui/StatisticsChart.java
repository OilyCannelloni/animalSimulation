package animalSimulation.gui;

import animalSimulation.EpochStatistics;
import animalSimulation.IStatisticsObserver;
import animalSimulation.Simulation;
import animalSimulation.SimulationStatistics;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

import java.lang.reflect.Field;
import java.util.*;

public class StatisticsChart extends LineChart<Number, Number> implements IStatisticsObserver {
    public HashMap<String, Series<Number, Number>> series;
    private String activeChart = "epochAnimalCount";
    private Simulation activeSimulation;

    private final String[] plottable = {
            "epochAnimalCount",
            "epochPlantCount",
            "epochAvgEnergy",
            "epochAvgDeadLifespan",
            "epochAvgChildCount"
    };

    public StatisticsChart(Simulation simulation) {
        super(new NumberAxis(), new NumberAxis());
        this.activeSimulation = simulation;
        this.setAnimated(false);
        this.series = new HashMap<>();
        for (String name : plottable) {
            Series<Number, Number> s = new Series<>();
            s.setName(name);
            this.series.put(name, s);
        }
    }

    public void setActiveChart(String name) {
        this.activeChart = name;
    }

    public void update() {
        this.getData().clear();
        this.getData().add(this.series.get(this.activeChart));
    }

    public void loadSimulation(Simulation simulation) {
        this.activeSimulation.statistics.removeObserver(this);
        this.activeSimulation = simulation;

        LinkedList<EpochStatistics> simulationLog = simulation.statistics.simulationLog;
        for (Series<Number, Number> s : this.series.values()) {
            s.getData().clear();
        }

        ListIterator<EpochStatistics> iterator = simulationLog.listIterator();
        while (iterator.hasNext()) {
            int index = iterator.nextIndex();
            this.entryLogged(iterator.next(), index);
        }

        this.activeSimulation.statistics.addObserver(this);
    }

    @Override
    public void entryLogged(EpochStatistics entry, int epoch) {
        for (Map.Entry<String, Series<Number, Number>> seriesEntry : this.series.entrySet()) {
            try {
                Field f = EpochStatistics.class.getField(seriesEntry.getKey());
                Number value = (Number) f.get(entry);
                Platform.runLater(() -> seriesEntry.getValue().getData().add(new Data<>(epoch, value)));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
