package animalSimulation.gui;

import animalSimulation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public JungleMap map;
    public MapGridPane grid;
    private final int epochs = Integer.MAX_VALUE;
    private Simulation simulation;
    public Thread simulationThread, gridUpdateThread;
    public final Object gridUpdatePauseLock = new Object();

    @Override
    public void start(Stage primaryStage) {
        this.init();
        Scene scene = new Scene(
                this.grid,
                this.grid.getDimensions().x,
                this.grid.getDimensions().y
        );

        this.gridUpdateThread = new Thread(this::syncUpdateGrid);
        this.gridUpdateThread.start();

        this.simulationThread = new Thread(this.simulation);
        this.simulationThread.start();

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

        this.simulation = new Simulation(this, this.map, imageManager);
        this.grid = new MapGridPane(map);

        AnimalFactory animalFactory = new AnimalFactory(this.map, imageManager,200, 1);
        for (int i = 0; i < 20; i++) {
            Vector2d position = Algorithm.getRandomEmptyFieldOutside(this.map, this.map.getJungleBox());
            animalFactory.createPlace(position);
        }
        this.updateGrid();
    }
}
