package Tests;

import modules.BloomFilter;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

class BloomFilterTest {
    private static int testWeight = 100;
    private static double accuracy = .1;

    public static void main(String[] args) {
//        optimalKFullTest();
        BloomFilter b = BloomFilter.fromFile("mises.txt");
//        falseNegativeTest(b);
//        falsePositiveFullTest(b);
        optimalNtest();
    }

    private static void optimalNtest() {
        int inputDataSize = 1000;
        System.out.print("Teste de N optimo em execução: ");
        boolean passed = true;
        for (int size = inputDataSize; size < inputDataSize * 10; size += inputDataSize) {
            BloomFilter dataSetFilter = createRandomFilter(size, BloomFilter.optimalK(size, inputDataSize), inputDataSize);
            double result = falsePositiveTest(dataSetFilter);
            if (Math.abs(result - dataSetFilter.probErr(inputDataSize)) > .1 && (size / inputDataSize) > 6)
                passed = false;
            if (size % (10 / 10) == 0) {
                System.out.printf("%3.3s, ", ((double) size / (inputDataSize * 10)));
            }
        }
        System.out.println();
        if (passed)
            System.out.println("Passou !");
        else
            System.out.println("Não passou no teste de N ótimo");
    }

    private static void falsePositiveFullTest(BloomFilter b) {
        System.out.print("Teste de falsos positivos em execução: ");
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
            System.out.println("Diferença entre o valor teorico e pratico de falsos positivos consistentemente alta");
            System.out.printf("%s erros em %s testes", numErros, testWeight);
        } else {
            System.out.println("Passou !");
        }
    }

    private static double falsePositiveTest(BloomFilter b) {
        int count = 0;
        for (var i = 0; i < 10000; i++) {
            boolean a = b.isElement(randomString());
            if (b.isElement(randomString())) count++;
        }

        return (double) count / 10000;
    }

    private static void falseNegativeTest(BloomFilter b) {
        System.out.print("Teste de falsos negativos em execução ");
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
            System.out.println("Falso Negativo Detetado");
        } else {
            System.out.println("Passou !");
        }
    }


    private static BloomFilter createRandomFilter(int n, int k, int numElem) {
        var b = new BloomFilter(n, k);
        for (var i = 0; i < numElem; i++)
            b.addElement(randomString());
        return b;
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


    private static void optimalKFullTest() {
        System.out.print("Teste de falsos positivos em execução: ");
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
            System.out.println("Erro na função de K otimo");
            System.out.printf("%s erros em %s testes", numErros, testWeight);
        } else {
            System.out.println("Passou !");
        }
    }

    private static TestResult optimalKTest(int n, int numElem) {
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
