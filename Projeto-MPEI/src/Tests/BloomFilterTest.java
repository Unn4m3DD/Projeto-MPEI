package Tests;

import modules.BloomFilter;

import java.io.File;
import java.util.*;

class BloomFilterTest {
    static int testWeight = 100;
    static double accuracy = .1;
    static int n = 8;


    public static void main(String[] args) {
        optimalKFullTest();
        n = optimalNtest(accuracy);
        falseNegativeTest();
        falsePositiveFullTest();
    }

    static int optimalNtest(double thr) {
        int inputDataSize = 1000;
        System.out.println("Optimal N test in execution ");
        for (int i = 1; true; i++) {
            try {
                BloomFilter dataSetFilter = createRandomFilter(i * inputDataSize, BloomFilter.optimalK(i * inputDataSize, inputDataSize), inputDataSize);
                double result = falsePositiveTest(dataSetFilter, new HashSet<>());
                if (Math.abs(result) < thr) {
                    System.out.printf("Minimal N for error < %5.5s%s : %s\n", thr * 100, "%", i);
                    return i;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    static void falsePositiveFullTest() {
        System.out.print("False positive test in execution ");
        BloomFilter b = BloomFilter.fromFile("mises.txt", n);
        File book = new File("mises.txt");
        Set<String> wordSet = new HashSet<>();
        try (Scanner k = new Scanner(book)) {
            while (k.hasNext()) {
                wordSet.add(k.next());
            }
        } catch (Exception e) {
            System.out.println("Runtime Error: " + e.toString());
        }
        var numErros = 0;
        for (var i = 0; i < testWeight; i++) {
            var result = falsePositiveTest(b, wordSet);
            if (Math.abs(result - b.probErr(wordSet.size())) > .1) numErros++;
            if (i % (testWeight / 10) == 0) {
                System.out.printf("%3.3s, ", ((double) i / testWeight));
            }
        }
        System.out.println();
        if (numErros != 0 && ((double) numErros / testWeight) > accuracy) {
            System.out.println("Difference between theoretical and practical values for false positives consistently high");
            System.out.printf("%s errors in %s tests", numErros, testWeight);
        } else {
            System.out.println("Passed !");
        }
    }

    static double falsePositiveTest(BloomFilter b, Set<String> wordSet) {
        int count = 0;
        for (var i = 0; i < 1000000/2; i++) {
            String s = randomString();
            if (b.isElement(s) && !wordSet.contains(s)) count++;
        }

        return (double) count / 1000000/2;
    }

    static void falseNegativeTest() {
        System.out.print("False negatives test in execution ");
        BloomFilter b = BloomFilter.fromFile("mises.txt", n);
        File book = new File("mises.txt");
        Set<String> wordSet = new HashSet<>();
        try (Scanner k = new Scanner(book)) {
            while (k.hasNext()) {
                wordSet.add(k.next());
            }
        } catch (Exception e) {
            System.out.println("Runtime Error: " + e.toString());
        }
        var erro = false;
        for (var elem : wordSet) {
            if (!b.isElement(elem)) erro = true;
        }
        System.out.println();
        if (erro) {
            System.out.println("A false negative has been detected");
        } else {
            System.out.println("Passed !");
        }
    }


    static BloomFilter createRandomFilter(int n, int k, int numElem) {
        var b = new BloomFilter(n, k);
        for (var i = 0; i < numElem; i++)
            b.addElement(randomString());
        return b;
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


    static void optimalKFullTest() {
        int maxDiffofK = 2;
        System.out.print("False positive test in execution ");
        var numErros = 0;
        for (var i = 0; i < testWeight; i++) {
            int r1 = (int) Math.round(Math.random() * 10000);
            int r2 = (int) Math.round(Math.random() * r1);
            var result = optimalKTest(r1, r2);
            if (Math.abs(result.experimental - result.teorico) > maxDiffofK) numErros++;
            if (i % (testWeight / 10) == 0) {
                System.out.printf("%3.3s, ", ((double) i / testWeight));
            }
        }
        System.out.println();
        if (numErros != 0 && ((double) numErros / testWeight) > accuracy) {
            System.out.println("Error in optimal K function");
            System.out.printf("%s errors in %s tests", numErros, testWeight);
        } else {
            System.out.println("Passed !");
        }
    }

    static TestResult optimalKTest(int n, int numElem) {
        double c_best_err = 1;
        var c_best_index = -1;
        for (var j = 1; j < 10; j++) {
            var b = createRandomFilter(n, j, numElem);
            var count = 0;
            for (var i = 0; i < 10000; i++) {
                if (b.isElement(randomString())) count++;
            }
            var err = (double) count / 10000;
            if (c_best_err > err) {
                c_best_err = err;
                c_best_index = j;
            }
        }
        if (c_best_err == 0 || BloomFilter.optimalK(n, numElem) > 10) return new TestResult(0, 0);
        return new TestResult(BloomFilter.optimalK(n, numElem), c_best_index);
    }

}

class TestResult {
    public TestResult(double teorico, double experimental) {
        this.teorico = teorico;
        this.experimental = experimental;
    }

    double teorico, experimental;
}
