package Tests;

import modules.BloomFilter;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

class BloomFilterTest {
    static int testWeight = 100;
    static double accuracy = .1;
    static int n;

    public static void main(String[] args) {
        optimalKFullTest();
        n = optimalNtest();
        BloomFilter b = BloomFilter.fromFile("mises.txt", n);
        falseNegativeTest(b);
        falsePositiveFullTest(b);
    }

    static int optimalNtest() {
        int inputDataSize = 1000;
        System.out.println("Optimal N test in execution ");
        for (int i = 1; true; i++) {
            try {
                BloomFilter dataSetFilter = createRandomFilter(i * inputDataSize, BloomFilter.optimalK(i * inputDataSize, inputDataSize), inputDataSize);
                double result = falsePositiveTest(dataSetFilter);
                if (Math.abs(result) < .02) {
                    System.out.println("Minimal N for error < 0.1 : " + i);
                    return i;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    static void falsePositiveFullTest(BloomFilter b) {
        System.out.print("False positive test in execution ");
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
            var result = falsePositiveTest(b);
            if (Math.abs(result - b.probErr(wordSet.size())) > .1) numErros++;
            if (i % (testWeight / 10) == 0) {
                System.out.printf("%3.3s, ", ((double) i / testWeight));
            }
        }
        System.out.println();
        if (numErros != 0 && ((double) numErros / testWeight) > accuracy) {
            System.out.println("Difference between practical and theoretical value consistently high");
            System.out.printf("%s errors in %s tests", numErros, testWeight);
        } else {
            System.out.println("Passed !");
        }
    }

    static double falsePositiveTest(BloomFilter b) {
        int count = 0;
        for (var i = 0; i < 1000000; i++) {
            boolean a = b.isElement(randomString());
            if (b.isElement(randomString())) count++;
        }

        return (double) count / 1000000;
    }

    static void falseNegativeTest(BloomFilter b) {
        System.out.print("Test for false negatives in execution ");
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
            System.out.println("False negative detected");
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
        System.out.print("Test for optimal K in execution ");
        var numErros = 0;
        for (var i = 0; i < testWeight; i++) {
            int r1 = (int) Math.round(Math.random() * 10000);
            int r2 = (int) Math.round(Math.random() * r1);
            var result = optimalKTest(r1, r2);
            if (Math.abs(result.experimental - result.teorico) > 2) numErros++;
            if (i % (testWeight / 10) == 0) {
                System.out.printf("%3.3s, ", ((double) i / testWeight));
            }
        }
        System.out.println();
        if (numErros != 0 && ((double) numErros / testWeight) > accuracy) {
            System.out.println("Error in otimal K test");
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
        return new TestResult(c_best_index, BloomFilter.optimalK(n, numElem));
    }

}

class TestResult {
    public TestResult(double teorico, double experimental) {
        this.teorico = teorico;
        this.experimental = experimental;
    }

    double teorico, experimental;
}
