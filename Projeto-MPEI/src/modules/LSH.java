package modules;

import java.util.Arrays;

public class LSH {
    int[] fingerPrint;
    int numberOfGroups, numberOfElements;

    public LSH(MinHash minHash, int numberOfGroups) {
        int numberOfElements = minHash.getSignature().length / numberOfGroups;
        if (minHash.getSignature().length % numberOfElements != 0) numberOfGroups++;
        fingerPrint = new int[numberOfGroups];
        for (int i = 0; i < minHash.getSignature().length; i += numberOfElements) {
            for (int j = 0; j < numberOfElements; j++) {
                if (i + j < minHash.getSignature().length)
                    fingerPrint[i / numberOfElements] += minHash.getSignature()[i + j];
            }
        }
        this.numberOfElements = numberOfElements;
        this.numberOfGroups = numberOfGroups;
    }

    boolean isCandidate(LSH other, double simIndex) {
        double probColision = 1 - Math.pow(Math.pow(1 - simIndex, numberOfElements), numberOfGroups);
        int count = 0;
        for (int i = 0; i < numberOfGroups; i++) {
            if (other.fingerPrint[i] == fingerPrint[i]) count++;
        }
        return (double) count / numberOfGroups > probColision;
    }

}
