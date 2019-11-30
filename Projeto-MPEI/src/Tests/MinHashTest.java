package Tests;

import modules.MinHash;

import java.util.LinkedList;

class MinHashTest {
    public static void main(String[] args) {
        int values = Integer.MAX_VALUE / 100;
        int leftBound = Integer.MIN_VALUE/100;
        int rightBound = Integer.MAX_VALUE/100;
        long totalSize = rightBound + Math.abs(leftBound);
        int numberOfDivisions = 1000;
        long sizeOfDivision = (int) (totalSize/numberOfDivisions);
        var divisions = new int[numberOfDivisions];
        for (int i = 0; i < numberOfDivisions; i++) {
            divisions[i] = 0;
        }
        for (int i = 0; i < values; i++) {
            var valueList = new LinkedList<Integer>();
            valueList.add((int) Math.random() * Integer.MAX_VALUE);
            int hash =new MinHash(valueList).getSignature()[0];
            int div = 0; //div being the index of the division that this element is a part of
            while((leftBound + div*sizeOfDivision) < hash) {
                div++;
            }
            divisions[div--]++;
            System.out.println(i+"th element added to "+div+"division");
        }

        for (int i = 0; i < numberOfDivisions; i++) {
            System.out.println(divisions[i] * 100 / totalSize + "%");
        }
    }
}

