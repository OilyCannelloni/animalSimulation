package animalSimulation;

import java.io.File;
import java.util.Random;

public class World {
    public Random random;

    public static void main(String[] args) {

        JungleMap map = new JungleMap(
                100,
                30,
                new Rect2D(
                        new Vector2d(45, 45),
                        new Vector2d(55, 55)
                )
        );

        Animal a1 = new Animal(map, new Vector2d(15, 15), 10);
        map.placeElement(a1);

        MapVisualizer visualizer = new MapVisualizer(map);
        SimulationEngine engine = new SimulationEngine(visualizer, map, 100);
        engine.run(100);
    }
}
