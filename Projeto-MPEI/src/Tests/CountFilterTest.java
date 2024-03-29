package Tests;

import modules.BloomFilter;
import modules.CountFilter;

import java.util.ArrayList;
import java.util.Random;

public class CountFilterTest {
    public static int dataSetSize = 100000;
    public static int testWeight = 10000;
    public static double accuracy = 0.1;

    public static void main(String[] args) {
        testFalsePos();
        testFalseNeg();
    }

    static void testFalseNeg() {
        System.out.println("False negative test in execution ");
        int n = dataSetSize * 10;
        int k = BloomFilter.optimalK(n, dataSetSize);
        ArrayList<String> elements = new ArrayList<>(dataSetSize);
        CountFilter data = new CountFilter(n, k);
        for (int i = 0; i < dataSetSize; i++) {
            String newElem = randomString();
            int timesAdd = (int) (Math.random() * 100);
            for (int j = 0; j < timesAdd; j++) {
                data.addElement(newElem);
            }
            int timesRemove = (int) (Math.random() * 40);
            for (int j = 0; j < timesRemove; j++) {
                data.remElement(newElem);
            }
            if (timesAdd > timesRemove)
                elements.add(newElem);
            if (i % (dataSetSize / 5) == 0)
                System.out.printf("%3.3s, ", ((double) i / dataSetSize / 2));
        }
        int count = 0;
        for (int i = 0; i < elements.size(); i++) {
            if (!data.isElement(elements.get(i))) {
                count++;
            }
            if (i % (elements.size() / 5) == 0)
                System.out.printf("%3.3s, ", (0.5 + (double) i / elements.size() / 2));

        }
        System.out.println();
        if ((double) count / elements.size() < 0.1)
            System.out.println("Passed !");
        else
            System.out.println("Didn't pass false negative test");
    }

    static void testFalsePos() {
        System.out.println("False positive test in execution ");
        int n = dataSetSize * 10;
        int k = BloomFilter.optimalK(n, dataSetSize);
        ArrayList<String> elements = new ArrayList<>();
        CountFilter data = new CountFilter(n, k);
        for (int i = 0; i < dataSetSize; i++) {
            String newElem = randomString();
            data.addElement(newElem);
            elements.add(newElem);
            if (i % (dataSetSize / 5) == 0)
                System.out.printf("%3.3s, ", (double) i / dataSetSize / 2);
        }
        int counter = 0;
        for (int i = 0; i < testWeight; i++) {
            String testString = randomString();
            if (data.isElement(testString) && !elements.contains(testString))
                counter++;
            if (i % (testWeight / 5) == 0)
                System.out.printf("%3.3s, ", (0.5 + (double) i / testWeight / 2));
        }
//        System.out.println(counter);
        System.out.println();
        if (((double) counter / testWeight) < accuracy)
            System.out.println("Passed !");
        else
            System.out.println("Didn't pass false positive test");
    }

    static String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 50;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

}
