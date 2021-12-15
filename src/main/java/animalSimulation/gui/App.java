package animalSimulation.gui;

import animalSimulation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    public JungleMap map;
    public MapGridPane grid;
    private final int epochs = Integer.MAX_VALUE;
    private Simulation activeSimulation;
    public Thread simulationThread, gridUpdateThread;
    public final Object gridUpdatePauseLock = new Object();

    @Override
    public void start(Stage primaryStage) {
        this.init();


        ArrayList<SelectBoxButton> simSelectButtons = new ArrayList<>();
        simSelectButtons.add(new SelectBoxButton((e) -> System.out.println("button 1")));
        simSelectButtons.add(new SelectBoxButton((e) -> System.out.println("button 2")));

        HSelectBox simulationSelect = new HSelectBox(
                400,
                200,
                simSelectButtons
        );

        ToggleButton pauseButton = new ToggleButton(
                (e) -> {
                    System.out.println("pauseButton");
                    this.activeSimulation.togglePause();
                    synchronized (this.activeSimulation.pauseLock) {
                        this.activeSimulation.pauseLock.notifyAll();
                    }
                    if (this.activeSimulation.paused) this.reloadGrid();
                },
                "Unpause Simulation",
                "Pause Simulation"
        );

        VBox controlBox = new VBox(simulationSelect, pauseButton);
        VBox statBox = new VBox();
        VBox graphBox = new VBox();

        HBox optionsBox = new HBox(controlBox, statBox, graphBox);

        VBox SceneVBox = new VBox(this.grid, optionsBox);

        Scene scene = new Scene(
                SceneVBox
        );

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void syncUpdateGrid() {
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
        }
    }

    private void updateGrid() {
        for (Vector2d position : this.map.getUpdatedFields()) {
            this.grid.getField(position).update(map.ElementsAt(position));
        }
        this.map.clearUpdatedFields();
    }

    private void reloadGrid() {
        this.grid.clear();
        this.grid.draw();
    }


    public void init() {
        this.map = new JungleMap(
                100,
                30,
                new Rect2D(
                        new Vector2d(45, 10),
                        new Vector2d(55, 20)
                )
        );

        ImageManager imageManager = new ImageManager();
        imageManager.load();

        this.activeSimulation = new Simulation(this, this.map, imageManager);
        this.grid = new MapGridPane(map);

        AnimalFactory animalFactory = new AnimalFactory(this.map, imageManager,200, 1);
        for (int i = 0; i < 20; i++) {
            Vector2d position = Algorithm.getRandomEmptyFieldOutside(this.map, this.map.getJungleBox());
            animalFactory.createPlace(position);
        }
        this.updateGrid();

        this.gridUpdateThread = new Thread(this::syncUpdateGrid);
        this.gridUpdateThread.start();

        this.simulationThread = new Thread(this.activeSimulation);
        this.simulationThread.start();
    }
}
