package animalSimulation.gui;

import animalSimulation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private JungleMap map;
    private MapGridPane grid;

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
}
