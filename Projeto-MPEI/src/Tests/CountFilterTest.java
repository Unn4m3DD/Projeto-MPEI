package Tests;

import modules.BloomFilter;
import modules.CountFilter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class CountFilterTest {
    public static int dataSetSize = 100000;
    public static int testWeight = 10000;
    public static double accuracy = 0.1;

    public static void main(String[] args) {
        testFalsePos();
        testFalseNeg();
    }

    private static void testFalseNeg() {
        System.out.println("Teste de falsos negativos em execução");
        int n = dataSetSize * 10;
        int k = BloomFilter.optimalK(n, dataSetSize);
        ArrayList<String> elements = new ArrayList<>(dataSetSize);
        CountFilter data = new CountFilter(n, k);
        // TODO: 02/12/2019 checkar se optimalK de countfilter é igual ao optimal K de bloom filter
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
//        System.out.println((double)count/elements.size());
        if ((double) count / elements.size() < 0.1)
            System.out.println("Passou !");
        else
            System.out.println("Não passou o teste de falsos negativos");
    }

    private static void testFalsePos() {
        //TODO revamp com o modelo do anterior
        System.out.print("Teste de falsos positivos em execução:");
        int n = dataSetSize * 10;
        int k = BloomFilter.optimalK(n, dataSetSize);
        LinkedList elements = new LinkedList();
        CountFilter data = new CountFilter(n, k);
        for (int i = 0; i < dataSetSize; i++) {
            String newElem = randomString();
            data.addElement(newElem);
            elements.add(newElem);
            if (i % (dataSetSize + testWeight) / 10 == 0)
                System.out.print(".");
        }
        int counter = 0;
        for (int i = 0; i < testWeight; i++) {
            String testString = randomString();
            if (data.isElement(testString) && !elements.contains(testString))
                counter++;
            if (i % (dataSetSize + testWeight) / 10 == 0)
                System.out.print(".");
        }
        System.out.println();
        if (((double) counter / testWeight) > accuracy)
            System.out.println("Passou !");
        else
            System.out.println("Não passou o teste de falsos positivos");
    }

    private static String randomString() {
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
