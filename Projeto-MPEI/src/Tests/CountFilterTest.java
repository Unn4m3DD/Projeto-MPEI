package Tests;

import modules.BloomFilter;
import modules.CountFilter;

import java.util.LinkedList;
import java.util.Random;

public class CountFilterTest {
    public static int dataSetSize = 1000;
    public static int testWeight = 10000;
    public static double accuracy = 0.1;
    public static void main(String[] args) {
        testFalsePos();
        testFalseNeg();
    }

    private static void testFalseNeg() {
        //TODO check if this is okay
        System.out.println("Initializing false negative test for count filter");
        int n = dataSetSize * 10;
        int k = BloomFilter.optimalK(n, dataSetSize);
        LinkedList<String> elements = new LinkedList();
        CountFilter data = new CountFilter(n, k);
        for(int i=0; i<dataSetSize; i++){
            String newElem = randomString();
            data.addElement(newElem);
            elements.add(newElem);
            if(i%(dataSetSize+testWeight)/10 == 0)
                System.out.print(".");
        }
        boolean passed = true;
        for (int i = 0; i < elements.size(); i++) {
            if(! data.isElement(elements.get(i))){
                passed=false;
                break;
            }
            if(i%(dataSetSize+testWeight)/10 == 0)
                System.out.print(".");
        }
        System.out.println();
        if (passed)
            System.out.println("Passed !");
        else
            System.out.println("Didn't pass false positive test");
        }

    private static void testFalsePos() {
        //TODO check if this is actually okay
        System.out.print("Initializing false positive test for count filter");
        int n = dataSetSize * 10;
        int k = BloomFilter.optimalK(n, dataSetSize);
        LinkedList elements = new LinkedList();
        CountFilter data = new CountFilter(n, k);
        for (int i = 0; i < dataSetSize; i++) {
            String newElem = randomString();
            data.addElement(newElem);
            elements.add(newElem);
            if(i%(dataSetSize+testWeight)/10 == 0)
                System.out.print(".");
        }
        int counter = 0;
        for (int i = 0; i < testWeight; i++) {
            String testString = randomString();
            if (data.isElement(testString) && !elements.contains(testString))
                counter ++;
            if(i%(dataSetSize+testWeight)/10 == 0)
                System.out.print(".");
        }
        System.out.println();
        if (((double) counter / testWeight) > accuracy)
            System.out.println("Passed !");
        else
            System.out.println("Didn't pass false positive test");
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
