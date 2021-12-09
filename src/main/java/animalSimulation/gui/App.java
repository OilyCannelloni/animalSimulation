package animalSimulation.gui;

import animalSimulation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public JungleMap map;
    public MapGridPane grid;
    private int epochs = Integer.MAX_VALUE;
    private Engine engine;

    @Override
    public void start(Stage primaryStage) {
        this.init();
        Scene scene = new Scene(
                this.grid,
                this.grid.getDimensions().x,
                this.grid.getDimensions().y
        );

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    Platform.runLater(this::processTurn);
                } catch (InterruptedException ignore) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        thread.setDaemon(true);
        thread.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void processTurn() {
        // apply simulation
        this.engine.processTurn();

        // update map
        for (Vector2d position : this.map.getUpdatedFields()) {
            this.grid.getField(position).update(map.ElementsAt(position));
        }

        // tidy up
        this.engine.endTurn();
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

        this.engine = new Engine(this.map, imageManager);
        this.grid = new MapGridPane(map);

        AnimalFactory animalFactory = new AnimalFactory(this.map, imageManager,50, 1);
        for (int i = 0; i < 20; i++) {
            Vector2d position = Algorithm.getRandomEmptyFieldOutside(this.map, this.map.getJungleBox());
            animalFactory.createPlace(position);
        }
    }
}
