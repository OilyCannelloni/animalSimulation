package animalSimulation;

import java.util.Random;

public class Algorithm {
    private static final Random random = new Random(System.currentTimeMillis());

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

    public static void partitionIntoK(int n, int k) {

    }

    public static int[] generateRandomGenome(int length, int nVariants) {
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = random.nextInt(nVariants);
        }
        return res;
    }

    public static int[] intersectGenome(int[] g1, int[] g2) {
        return new int[Animal.genomeLength];
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
                System.out.println("getRandomEmptyField: exceeded max trials");
                return new Vector2d(0, 0);
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
}
