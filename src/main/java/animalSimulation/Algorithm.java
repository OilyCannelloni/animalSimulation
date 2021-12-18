package animalSimulation;

import javafx.util.Pair;

import java.util.*;

public class Algorithm {
    public static final Random random = new Random(System.currentTimeMillis());

    public static void shuffleArray(int[] array, int firstN) {
        if (firstN >= array.length) return;
        int index, temp;
        for (int i = 0; i < firstN; i++) {
            index = i + random.nextInt(array.length - i);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public static double map(double x, double a1, double b1, double a2, double b2) {
        double p = (x - a1)/(b1 - a1);
        return a2 + p*(b2 - a2);
    }

    public static int[] generateRandomGenome(int length, int nVariants) {
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = random.nextInt(nVariants);
        }
        // Arrays.sort(res);
        return res;
    }

    public static int[] intersectGenome(Animal a1, Animal a2) {
        int[] g1 = a1.getGenome(), g2 = a2.getGenome();
        assert g1.length == g2.length;
        double e1 = a1.getEnergy(), e2 = a2.getEnergy();
        double prop = e1 / (e1 + e2);
        int a1geneN = (int) map(prop, 0, 1, 0, g1.length);
        boolean fromLeft = random.nextBoolean();
        int[] res = new int[g1.length];

        if (fromLeft) {
            System.arraycopy(g1, 0, res, 0, a1geneN);
            System.arraycopy(g2, a1geneN, res, a1geneN, g1.length - a1geneN);
        } else {
            System.arraycopy(g1, g1.length - a1geneN, res, g1.length - a1geneN, a1geneN);
            System.arraycopy(g2, 0, res, 0, g1.length - a1geneN);
        }

        return res;
    }

    public static int getRandom(int[] arr) {
        return arr[random.nextInt(arr.length)];
    }

    public static Vector2d getRandomField(IWorldMap map) {
        return getRandomField(map, map.getBoundingBox());
    }

    public static Vector2d getRandomField(IWorldMap map, Rect2D region) {
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
            position = getRandomField(map, region);
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

    public static Pair<int[], LinkedList<Animal>> getDominantGenomeAnimals(IWorldMap map) {
        LinkedHashMap<int[], LinkedList<Animal>> animalsWithGenome = new LinkedHashMap<>();

        for (IMovableElement me : map.getMovableElements()) {
            if (me instanceof Animal) {
                Animal a = (Animal) me;
                int[] genome = a.getGenome();
                Arrays.sort(genome);
                animalsWithGenome.putIfAbsent(genome, new LinkedList<>());
                animalsWithGenome.get(genome).add(a);
            }
        }

        if (animalsWithGenome.isEmpty()) {
            int[] ret = new int[32];
            Arrays.fill(ret, 0);
            return new Pair<>(ret, new LinkedList<>());
        }

        Map.Entry<int[], LinkedList<Animal>> maxEntry = Collections.max(
                animalsWithGenome.entrySet(),
                Map.Entry.comparingByValue(Comparator.comparingInt(LinkedList::size))
        );
        return new Pair<>(maxEntry.getKey(), maxEntry.getValue());
    }
}
