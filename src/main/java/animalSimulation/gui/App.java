package animalSimulation.gui;

import animalSimulation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class App extends Application {
    public HashMap<String, IWorldMap> maps = new HashMap<>();
    public MapGridPane grid;
    private final int epochs = Integer.MAX_VALUE;
    private String activeWorld;
    private final HashMap<String, Simulation> simulations = new HashMap<>();
    private final HashMap<String, StatDisplayBox> statDisplayBoxes = new HashMap<>();
    private final HashMap<String, StatDisplayBox> trackStatDisplayBoxes = new HashMap<>();
    public Thread guiUpdateThread;
    public final Object gridUpdatePauseLock = new Object();
    private ToggleButton pauseButton, dominantGenomeSelectButton;
    private ClickButton saveStatisticsButton;

    @Override
    public void start(Stage primaryStage) {
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
                    System.out.println("pauseButton");
                    Simulation sim = this.simulations.get(this.activeWorld);
                    sim.togglePause();
                    if (!sim.paused) synchronized (sim.pausePauseLock) {
                        this.getActiveMap().removeAllElementsOfType(HighlightCircle.class);
                        this.dominantGenomeSelectButton.setInactive();
                        sim.pausePauseLock.notifyAll();
                    }
                    if (sim.paused) this.reloadGrid();
                    System.out.printf("Simulation %s's pause set to %b", this.activeWorld, sim.paused);
                },
                "Unpause Simulation",
                "Pause Simulation"
        );

        this.dominantGenomeSelectButton = new ToggleButton(
                (e) -> {
                    System.out.println("highlightButton");
                    if (!this.pauseButton.isActive) return;
                    IWorldMap activeMap = this.getActiveMap();

                    Pair<int[], LinkedList<Animal>> dominantAnimals =
                            Algorithm.getDominantGenomeAnimals(activeMap);
                    System.out.println(dominantAnimals.getValue().size());
                    for (Animal animal : dominantAnimals.getValue()) {
                        Vector2d position = animal.getPosition();
                        activeMap.placeElement(new HighlightCircle(activeMap, position));
                        this.grid.getField(position).update(activeMap.ElementsAt(position));
                    }
                },
                "Show animals with dominant genome",
                "Show animals with dominant genome"
        );

        this.saveStatisticsButton = new ClickButton(
                (e) -> {
                    System.out.println("statButton");
                    this.getActiveSimulation().statistics.saveToCSV(this.activeWorld);
                },
                "Save simulation statistics"
        );


        VBox controlBox = new VBox(simulationSelect, this.pauseButton, this.dominantGenomeSelectButton,
                this.saveStatisticsButton);

        // Statistics
        for (Field f : SimulationStatistics.class.getFields()) {
            if (f.getType().equals(Long.TYPE) || f.getType().equals(int[].class) || f.getType().equals(Float.TYPE))
                this.statDisplayBoxes.put(f.getName(), new StatDisplayBox(f.getName()));
        }
        VBox statBox = new VBox();
        statBox.getChildren().addAll(this.statDisplayBoxes.values());


        VBox graphBox = new VBox();

        // Track Statistics
        for (Field f : AnimalStatistics.class.getFields()) {
            this.trackStatDisplayBoxes.put(f.getName(), new StatDisplayBox(f.getName()));
        }
        VBox trackBox = new VBox();
        trackBox.getChildren().addAll(this.trackStatDisplayBoxes.values());

        HBox optionsBox = new HBox(controlBox, statBox, graphBox, trackBox);

        VBox SceneVBox = new VBox(this.grid, optionsBox);

        Scene scene = new Scene(
                SceneVBox
        );



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
    }

    private void updateGui() {
        while (true) {
            // sleep
            try {
                Thread.sleep(100);
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
    }

    private void initSetActiveWorld(String name) {
        Simulation newSim = this.simulations.get(name);
        newSim.isDisplayed = true;
        this.activeWorld = name;
        this.grid.setActiveMap(this.maps.get(this.activeWorld));
    }


    private void initialize() {
        ImageManager imageManager = new ImageManager();
        imageManager.load();

        JungleMap map1 = new JungleMap(
                100,
                30,
                new Rect2D(
                        new Vector2d(45, 10),
                        new Vector2d(55, 20)
                )
        );
        AnimalFactory animalFactory1 = new AnimalFactory(map1, imageManager,200, 1);
        for (int i = 0; i < 20; i++) {
            Vector2d position = Algorithm.getRandomEmptyFieldOutside(map1, map1.getJungleBox());
            animalFactory1.createPlace(position);
        }
        Simulation sim1 = new Simulation(this, map1, imageManager);
        this.addWorld(map1, sim1, "map1");


        JungleMap map2 = new JungleMap(
                100,
                30,
                new Rect2D(
                        new Vector2d(30, 10),
                        new Vector2d(70, 20)
                )
        );
        AnimalFactory animalFactory2 = new AnimalFactory(map2, imageManager,200, 1);
        for (int i = 0; i < 40; i++) {
            Vector2d position = Algorithm.getRandomEmptyFieldOutside(map2, map2.getJungleBox());
            animalFactory2.createPlace(position);
        }
        Simulation sim2 = new Simulation(this, map2, imageManager);
        this.addWorld(map2, sim2, "map2");


        JungleMap map3 = new JungleMap(
                100,
                30,
                new Rect2D(
                        new Vector2d(0, 0),
                        new Vector2d(15, 15)
                )
        );
        AnimalFactory animalFactory3 = new AnimalFactory(map3, imageManager,200, 1);
        for (int i = 0; i < 40; i++) {
            Vector2d position = Algorithm.getRandomEmptyFieldOutside(map3, map3.getJungleBox());
            animalFactory3.createPlace(position);
        }
        Simulation sim3 = new Simulation(this, map3, imageManager);
        this.addWorld(map3, sim3, "map3");


        this.grid = new MapGridPane(this, this.maps.get("map1"));
        this.setActiveWorld("map1");

        this.guiUpdateThread = new Thread(this::updateGui);
        this.guiUpdateThread.start();

        this.startWorld("map1");
        this.startWorld("map2");
        this.startWorld("map3");
    }

    public Simulation getActiveSimulation() {
        return this.simulations.get(this.activeWorld);
    }

    public IWorldMap getActiveMap() {
        return this.maps.get(this.activeWorld);
    }
}
