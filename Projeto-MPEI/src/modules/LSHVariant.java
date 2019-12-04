package modules;

public class LSHVariant {
    int[] fingerPrint;
    int numberOfGroups, numberOfElements;
    public String name;

    public LSHVariant(MinHash minHash, int numberOfGroups, String name) {
        this.name = name;
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

    public boolean isCandidate(LSHVariant other, double simIndex) {
        double probColision = 1 - Math.pow(1 - Math.pow(simIndex, numberOfElements), numberOfGroups);
        int count = 0;
        for (int i = 0; i < numberOfGroups; i++) {
            if (other.fingerPrint[i] == fingerPrint[i]) count++;
        }
        return (double) count / numberOfGroups > probColision;
    }

}
