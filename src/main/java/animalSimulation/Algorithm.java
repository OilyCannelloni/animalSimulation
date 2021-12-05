package animalSimulation;

import java.util.Random;

public class Algorithm {
    public static void shuffleArray(int[] array, int firstN) {
        if (firstN >= array.length) return;
        int index, temp;
        Random random = new Random(System.currentTimeMillis());
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
        Random random = new Random();
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = random.nextInt(nVariants);
        }
        return res;
    }
}
