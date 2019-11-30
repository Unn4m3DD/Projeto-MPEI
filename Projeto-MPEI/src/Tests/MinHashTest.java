package Tests;

import modules.MinHash;

import java.util.Arrays;
import java.util.LinkedList;

class MinHashTest {
    public static void main(String[] args) {
        int values = Integer.MAX_VALUE / 100;
        int leftBound = Integer.MIN_VALUE / 100;
        int rightBound = Integer.MAX_VALUE / 100;
        long totalSize = rightBound + Math.abs(leftBound);
        int numberOfDivisions = 1000;
        long sizeOfDivision = (int) (totalSize / numberOfDivisions);
        var divisions = new int[numberOfDivisions];
        Arrays.fill(divisions, 0);
        for (int i = 0; i < values; i++) {
            var valueList = new LinkedList<Integer>();
            valueList.add((int) ( Math.random() * Integer.MAX_VALUE));
            var hashSignature = new MinHash(valueList).getSignature();
            Arrays.sort(hashSignature);
            var hash = hashSignature[0];
            int div = 0; //div being the index of the division that this element is a part of
            while ((leftBound + (div * sizeOfDivision)) < hash) {
                div++;
            }
            divisions[div--]++;
        }

        System.out.println(Arrays.toString(divisions));
    }
}

