package animalSimulation;

import java.util.concurrent.TimeUnit;

public class SimulationEngine {
    private final MapVisualizer visualizer;
    private final IWorldMap map;
    private final int delayMillis;

    public SimulationEngine(MapVisualizer visualizer, IWorldMap map, int delayMillis) {
        this.visualizer = visualizer;
        this.map = map;
        this.delayMillis = delayMillis;
    }

    public void run(int epochs) {
        while (epochs-- >= 0) {
            // draw map
            this.visualizer.draw();

            // pause
            try {
                TimeUnit.MILLISECONDS.sleep(this.delayMillis);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

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
    }
}
