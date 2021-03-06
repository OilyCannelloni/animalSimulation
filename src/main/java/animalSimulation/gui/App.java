package animalSimulation.gui;

import animalSimulation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.lang.reflect.Field;
import java.util.*;

public class App extends Application {
    public int MAX_MAPS = 10;

    public HashMap<String, IWorldMap> maps = new LinkedHashMap<>();
    public MapGridPane grid;
    private String activeWorld;
    private final HashMap<String, Simulation> simulations = new LinkedHashMap<>();
    private final HashMap<String, StatDisplayBox> statDisplayBoxes = new HashMap<>();
    private final HashMap<String, StatDisplayBox> trackStatDisplayBoxes = new HashMap<>();
    public Thread guiUpdateThread;
    public final Object gridUpdatePauseLock = new Object();
    private ToggleButton pauseButton, dominantGenomeSelectButton;
    private StatisticsChart chart;
    private Stage primaryStage;
    private ImageManager imageManager;
    private int mapID = 0;

    private final String[] mapTypes = {
            "JungleMap",
            "WrappedJungleMap"
    };

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.imageManager = new ImageManager();
        imageManager.load();
        this.showInputPrompt();
    }

    private void showInputPrompt() {
        VBox mapListBox = new VBox();
        mapListBox.setPrefHeight(500);

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(new ClickButton((event -> {

            LinkedList<MapConfigurationBox> mapBoxes = new LinkedList<>();
            for (Node node : mapListBox.getChildren()) {
                if (node instanceof MapConfigurationBox) {
                    MapConfigurationBox mapBox = (MapConfigurationBox) node;
                    mapBoxes.add(mapBox);
                }
            }
            this.createWorldsFromInput(mapBoxes);

            Platform.runLater(this::createGUI);
        }), "Start Simulation"));

        buttonBox.getChildren().add(new ClickButton((event -> {
            if (mapListBox.getChildren().size() < this.MAX_MAPS)
                Platform.runLater(() -> mapListBox.getChildren().add(
                        new MapConfigurationBox(this.mapTypes, ++this.mapID)));
            else
                System.out.println("InputPrompt: Maximum number of maps reached.");
        }), "Add Map"));

        VBox mainBox = new VBox(mapListBox, buttonBox);
        mainBox.setPrefWidth(1600);
        Scene scene = new Scene(mainBox);
        Stage inputStage = new Stage();
        inputStage.setTitle("Configure Simulation");
        inputStage.setScene(scene);
        inputStage.show();
    }

    private void createWorldsFromInput(Collection<MapConfigurationBox> mapBoxes) {
        JungleMapFactory factory = new JungleMapFactory(this.imageManager);
        int i = 0;
        for (MapConfigurationBox mapBox : mapBoxes) {
            String mapName = mapBox.getMapName();
            i++;
            if (mapName == null) mapName = "map_" + i;

            String mapType = mapBox.getMapTypeName();
            JungleMap map = factory.createMap(mapType, mapName, mapBox.getMapData());
            map.initialize();
            Simulation simulation = new Simulation(this, map);

            this.addWorld(map, simulation, mapName);
        }


    }

    private void createGUI() {
        this.initialize();
        ArrayList<SelectBoxButton> simSelectButtons = new ArrayList<>();
        for (String worldName : this.maps.keySet()) {
            simSelectButtons.add(new SelectBoxButton(
                    worldName,
                    (e) -> this.setActiveWorld(worldName)
            ));
        }

        HSelectBox simulationSelect = new HSelectBox(
                400,
                200,
                simSelectButtons
        );

        this.pauseButton = new ToggleButton(
                (e) -> {
                    Simulation sim = this.simulations.get(this.activeWorld);
                    sim.togglePause();
                    if (!sim.paused) synchronized (sim.pausePauseLock) {
                        this.getActiveMap().removeAllElementsOfType(HighlightCircle.class);
                        this.dominantGenomeSelectButton.setInactive();
                        sim.pausePauseLock.notifyAll();
                    }
                    if (sim.paused) this.reloadGrid();
                },
                "Unpause Simulation",
                "Pause Simulation"
        );

        this.dominantGenomeSelectButton = new ToggleButton(
                (e) -> {
                    if (!this.pauseButton.isActive) return;
                    IWorldMap activeMap = this.getActiveMap();

                    Pair<Genome, LinkedList<Animal>> dominantAnimals =
                            Algorithm.getDominantGenomeAnimals(activeMap);
                    for (Animal animal : dominantAnimals.getValue()) {
                        Vector2d position = animal.getPosition();
                        activeMap.placeElement(new HighlightCircle(activeMap, position));
                        this.grid.getField(position).update(activeMap.ElementsAt(position));
                    }
                },
                "Show animals with dominant genome",
                "Show animals with dominant genome"
        );

        ClickButton saveStatisticsButton = new ClickButton(
                (e) -> {
                    this.getActiveSimulation().statistics.saveToCSV(this.activeWorld);
                },
                "Save simulation statistics"
        );


        VBox controlBox = new VBox(simulationSelect, this.pauseButton, this.dominantGenomeSelectButton,
                saveStatisticsButton);

        // Statistics
        for (Field f : SimulationStatistics.class.getFields()) {
            if (f.getType().equals(Long.TYPE) || f.getType().equals(int[].class) || f.getType().equals(Float.TYPE))
                this.statDisplayBoxes.put(f.getName(), new StatDisplayBox(f.getName()));
        }
        VBox statBox = new VBox();
        statBox.getChildren().addAll(this.statDisplayBoxes.values());

        // Track Statistics
        for (Field f : AnimalStatistics.class.getFields()) {
            this.trackStatDisplayBoxes.put(f.getName(), new StatDisplayBox(f.getName()));
        }
        VBox trackBox = new VBox();
        trackBox.getChildren().addAll(this.trackStatDisplayBoxes.values());

        // Chart
        this.chart = new StatisticsChart(this.getActiveSimulation());
        this.chart.loadSimulation(this.getActiveSimulation());
        this.chart.setActiveChart("epochAnimalCount");
        ChartSelectComboBox chartSelectBox = new ChartSelectComboBox(this.chart);
        chartSelectBox.setValue("epochAnimalCount");
        VBox graphBox = new VBox(chartSelectBox, this.chart);

        HBox optionsBox = new HBox(controlBox, statBox, graphBox, trackBox);

        VBox SceneVBox = new VBox(this.grid, optionsBox);

        Scene scene = new Scene(SceneVBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateStatistics() {
        SimulationStatistics stats = this.simulations.get(this.activeWorld).statistics;
        for (String fieldName : this.statDisplayBoxes.keySet()) {
            try {
                Field f = stats.getClass().getField(fieldName);
                if (f.getType() == int[].class) {
                    int[] arr = (int[]) f.get(stats);
                    this.statDisplayBoxes.get(fieldName).setValue(Arrays.toString(arr));
                    continue;
                }
                this.statDisplayBoxes.get(fieldName).setValue(f.get(stats).toString());
            } catch (NoSuchFieldException | IllegalAccessException ignore) {}
        }

        AnimalStatistics trackStats = this.simulations.get(this.activeWorld).tracker.getAnimalStatistics();
        for (String fieldName : this.trackStatDisplayBoxes.keySet()) {
            try {
                Field f = trackStats.getClass().getField(fieldName);
                if (f.getType() == int[].class) {
                    int[] arr = (int[]) f.get(trackStats);
                    this.trackStatDisplayBoxes.get(fieldName).setValue(Arrays.toString(arr));
                    continue;
                }
                this.trackStatDisplayBoxes.get(fieldName).setValue(f.get(trackStats).toString());
            } catch (NoSuchFieldException | IllegalAccessException ignore) {}
        }

        this.updateChart();
    }

    private void updateGui() {
        while (true) {
            // sleep
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignore) {}

            // wait for simulation turn to complete
            synchronized (this.gridUpdatePauseLock) {
                try {
                    this.gridUpdatePauseLock.wait();
                } catch (InterruptedException ignore) {}

                this.updateGrid();
            }

            this.updateStatistics();
        }
    }

    private void updateChart() {
        if (this.chart != null)
            Platform.runLater(() -> this.chart.update());
    }

    private void updateGrid() {
        Simulation activeSim = this.simulations.get(this.activeWorld);
        activeSim.isBeingRendered = true;

        IWorldMap activeMap = this.maps.get(this.activeWorld);
        for (Vector2d position : activeMap.getUpdatedFields()) {
            this.grid.getField(position).update(activeMap.ElementsAt(position));
        }
        activeMap.clearUpdatedFields();

        activeSim.isBeingRendered = false;
        synchronized (activeSim.renderPauseLock) {
            activeSim.renderPauseLock.notifyAll();
        }
    }

    private void reloadGrid() {
        Simulation activeSim = this.simulations.get(this.activeWorld);
        activeSim.isBeingRendered = true;

        this.grid.clear();
        this.grid.drawAll();

        activeSim.isBeingRendered = false;
        synchronized (activeSim.renderPauseLock) {
            activeSim.renderPauseLock.notifyAll();
        }
    }

    private void addWorld(IWorldMap map, Simulation simulation, String name) {
        this.maps.put(name, map);
        this.simulations.put(name, simulation);
    }

    private void startWorld(String name) {
        Thread simThread = new Thread(this.simulations.get(name));
        simThread.start();
    }

    private void setActiveWorld(String name) {
        if (this.activeWorld == null) {
            this.initSetActiveWorld(name);
            return;
        }
        this.simulations.get(this.activeWorld).isDisplayed = false;
        Simulation newSim = this.simulations.get(name);
        newSim.isDisplayed = true;
        this.activeWorld = name;
        this.grid.setActiveMap(this.maps.get(this.activeWorld));
        this.reloadGrid();
        if (newSim.paused) this.pauseButton.setActive();
        else this.pauseButton.setInactive();
        this.chart.loadSimulation(newSim);
    }

    private void initSetActiveWorld(String name) {
        Simulation newSim = this.simulations.get(name);
        newSim.isDisplayed = true;
        this.activeWorld = name;
        this.grid.setActiveMap(this.maps.get(this.activeWorld));
    }

    private void initialize() {
        String firstMap = this.maps.keySet().iterator().next();
        this.grid = new MapGridPane(this, this.maps.get(firstMap));
        this.setActiveWorld(firstMap);

        this.guiUpdateThread = new Thread(this::updateGui);
        this.guiUpdateThread.start();

        for (String name : this.simulations.keySet()) this.startWorld(name);
    }

    public Simulation getActiveSimulation() {
        return this.simulations.get(this.activeWorld);
    }

    public IWorldMap getActiveMap() {
        return this.maps.get(this.activeWorld);
    }
}
