package Tests;

import modules.CountFilter;

import java.util.LinkedList;
import java.util.Random;

public class CountFilterTest {
    public static int dataSetSize = 1000;
    public static int testWeight = 10000;

    public static void main(String[] args) {
        testFalsePos();
        //testFalseNeg();
    }

    private static void testFalsePos() {
        int n = dataSetSize * 10;
        int k = CountFilter.optimalK(n, dataSetSize);
        LinkedList elements = new LinkedList();
        CountFilter data = new CountFilter(n,k);
        for (int i = 0; i < dataSetSize; i++) {
            String newElem = randomString();
            data.addElement(newElem);
            elements.add(newElem);
        }
        boolean passed = true;
        for(int i=0; i<testWeight; i++){
            String testString = randomString();
            if(data.isElement(testString) && ! elements.contains(testString))
                passed = false;
        }
        if(passed)
            System.out.println("Test for false positives passed");
        else
            System.out.println("Test for false positives was not passed");
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
