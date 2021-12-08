package animalSimulation.gui;

import animalSimulation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class App extends Application implements IPositionChangeObserver {
    public JungleMap map;
    public MapGridPane grid;
    private int epochs = Integer.MAX_VALUE;

    @Override
    public void start(Stage primaryStage) {
        this.init();
        Scene scene = new Scene(
                this.grid,
                this.grid.getDimensions().x,
                this.grid.getDimensions().y
        );

        this.grid.draw();
        primaryStage.setScene(scene);
        primaryStage.show();

        Thread thread = new Thread(() -> {
            Runnable nextTurn = () -> {
                processTurn();
                grid.clear();
                grid.draw();
            };

            while (true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignore) {
                    Thread.currentThread().interrupt();
                }
                Platform.runLater(nextTurn);
            }
        });

        thread.setDaemon(true);
        thread.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void processTurn() {
        // remove dead bodies
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                if (a.getEnergy() <= 0) map.removeElement(a);
            }
        }

        // turn and move animals
        for (IMovableElement e : this.map.getMovableElements()) {
            if (e instanceof Animal) {
                Animal a = (Animal) e;
                a.turn();
                a.move();
            }
        }
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

        this.grid = new MapGridPane(map);

        Animal a1 = new Animal(this.map, imageManager, new Vector2d(15, 15), 30);
        map.placeElement(a1);
    }

    @Override
    public void positionChanged(IMapElement element, Vector2d oldPosition, Vector2d newPosition) {

    }
}
