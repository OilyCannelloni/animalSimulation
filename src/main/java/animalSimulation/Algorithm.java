package animalSimulation;

import javafx.util.Pair;
import java.util.*;

public class Algorithm {
    public static final Random random = new Random(System.currentTimeMillis());

    public static double map(double x, double a1, double b1, double a2, double b2) {
        double p = (x - a1)/(b1 - a1);
        return a2 + p*(b2 - a2);
    }

    public static int getRandom(int[] arr) {
        return arr[random.nextInt(arr.length)];
    }

    public static Vector2d getRandomField(Rect2D region) {
        return new Vector2d(
                region.lowerLeft.x + random.nextInt(region.getDimensions().x),
                region.lowerLeft.y + random.nextInt(region.getDimensions().y)
        );
    }

    public static Vector2d getRandomEmptyField(IWorldMap map) {
        return getRandomEmptyField(map, map.getBoundingBox());
    }

    public static Vector2d getRandomEmptyField(IWorldMap map, Rect2D region) {
        // needs better algo
        Vector2d position;
        int trials = 100;
        do {
            position = getRandomField(region);
            if (--trials == 0) {
                return null;
            }
        } while (map.isOccupied(position));
        return position;
    }

    public static Vector2d getRandomEmptyFieldOutside(IWorldMap map, Rect2D region) {
        Vector2d position;
        do {
            position = getRandomEmptyField(map);
        } while (region.contains(position));
        return position;
    }

    public static LinkedList<Animal> getAnimals(IWorldMap map) {
        LinkedList<Animal> animals = new LinkedList<>();
        for (IMovableElement me : map.getMovableElements()) {
            if (me instanceof Animal) {
                Animal a = (Animal) me;
                animals.add(a);
            }
        }
        return animals;
    }

    public static Animal getRandomAnimal(IWorldMap map) {
        LinkedList<Animal> animals = getAnimals(map);
        return animals.get(random.nextInt(animals.size()));
    }

    public static Animal getStrongestAnimal(LinkedList<IMapElement> elements) {
        if (elements == null) return null;
        Animal bestAnimal = null;
        int bestEnergy = -1;
        for (IMapElement element : elements) {
            if (element instanceof Animal) {
                Animal a = (Animal) element;
                if (a.getEnergy() > bestEnergy) {
                    bestEnergy = a.getEnergy();
                    bestAnimal = a;
                }
            }
        }
        return bestAnimal;
    }

    public static Pair<Genome, LinkedList<Animal>> getDominantGenomeAnimals(IWorldMap map) {
        LinkedHashMap<Genome, LinkedList<Animal>> animalsWithGenome = new LinkedHashMap<>();

        for (IMovableElement me : map.getMovableElements()) {
            if (me instanceof Animal) {
                Animal a = (Animal) me;
                Genome genome = a.getGenome();
                animalsWithGenome.putIfAbsent(genome, new LinkedList<>());
                animalsWithGenome.get(genome).add(a);
            }
        }

        if (animalsWithGenome.isEmpty()) {
            return new Pair<>(Genome.empty(), new LinkedList<>());
        }

        Map.Entry<Genome, LinkedList<Animal>> maxEntry = Collections.max(
                animalsWithGenome.entrySet(),
                Map.Entry.comparingByValue(Comparator.comparingInt(LinkedList::size))
        );
        return new Pair<>(maxEntry.getKey(), maxEntry.getValue());
    }
}
