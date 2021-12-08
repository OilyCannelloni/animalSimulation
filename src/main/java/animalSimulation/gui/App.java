package animalSimulation.gui;

import animalSimulation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;

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
                    Thread.sleep(300);
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
        for (Vector2d position : this.engine.changedPositions) {
            this.grid.getField(position).update(map.ElementsAt(position));
        }

        // tidy up
        this.engine.endTurn();
    }

    public void init() {
        ImageManager imageManager = new ImageManager();
        imageManager.load();



        this.map = new JungleMap(
                100,
                30,
                new Rect2D(
                        new Vector2d(45, 45),
                        new Vector2d(55, 55)
                )
        );

        this.engine = new Engine(this.map);

        this.grid = new MapGridPane(map);

        LinkedList<IPositionChangeObserver> observers = new LinkedList<>();
        observers.add(engine);
        Animal a1 = new Animal(this.map, imageManager, new Vector2d(15, 15), 30, observers);
        map.placeElement(a1);
        Animal a2 = new Animal(this.map, imageManager, new Vector2d(80, 20), 30, observers);
        map.placeElement(a2);
        Animal a3 = new Animal(this.map, imageManager, new Vector2d(15, 14), 30, observers);
        map.placeElement(a3);
    }
}
