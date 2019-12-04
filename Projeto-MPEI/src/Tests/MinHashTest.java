package Tests;

import modules.Hash;
import modules.HashSeed;
import modules.MinHash;
import util.Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

class MinHashTest {
    public static int shingleSize = 8;
    public static int fileSize;
    public static double accuracy = 0.05;
    public static int optimalK = Integer.MAX_VALUE;

    public static void main(String[] args) {
        optimalNumberOfHashes(accuracy);
        optimalSimilarity();
    }

    static void optimalSimilarity() {
        System.out.println("Initializing test for minhash quality");
        File[] files = createFiles(100);
        double[] similarity = new double[files.length - 1];
        MinHash originalfile = getMinHash(files[0], optimalK);
        for (int i = 0; i < similarity.length; i++) {
            similarity[i] = originalfile.calcSimTo(getMinHash(files[i + 1], optimalK));
            if (i % (similarity.length / 5) == 0)
                System.out.printf("%3.3s, ", ((double) i / similarity.length / 2));
        }
        double[] jaccardIndex = new double[similarity.length];
        for (int i = 0; i < jaccardIndex.length; i++) {
            jaccardIndex[i] = MinHash.jaccardIndex(files[0], files[i + 1], shingleSize);
            if (i % (similarity.length / 5) == 0)
                System.out.printf("%3.3s, ", (0.5 + (double) i / similarity.length / 2));
        }

        double sum = 0;
        for (int i = 0; i < similarity.length; i++) {
            sum += Math.abs(similarity[i] - jaccardIndex[i]);
        }
        System.out.println();
        sum /= similarity.length;
        if (sum < accuracy)
            System.out.println("Test for difference between Jaccard index and minhash similarity passed!");
        else
            System.out.println("Jaccard index and minhash calculations are too far apart");
    }

    static void optimalNumberOfHashes(double thr) {
        System.out.println("Initializing test for optimal number of hashes...");
        File[] files = createFiles(100);
        for (int seedSize = 50; seedSize <= 250; seedSize += 20) {
            double[] similarity = new double[files.length - 1];
            MinHash originalfile = getMinHash(files[0], seedSize);
            for (int i = 0; i < similarity.length; i++) {
                similarity[i] = originalfile.calcSimTo(getMinHash(files[i + 1], seedSize));
                if (i % (similarity.length / 5) == 0)
                    System.out.printf("%3.3s, ", (double) i / similarity.length / 2);
            }

            double[] jaccardIndex = new double[similarity.length];
            for (int i = 0; i < jaccardIndex.length; i++) {
                jaccardIndex[i] = MinHash.jaccardIndex(files[0], files[i + 1], shingleSize);
                if (i % (similarity.length / 5) == 0)
                    System.out.printf("%3.3s, ", (0.5 + (double) i / similarity.length / 2));
            }
            System.out.println("");

            double sum = 0;
            for (int i = 0; i < similarity.length; i++) {
                sum += Math.abs(similarity[i] - jaccardIndex[i]);
            }
            sum /= similarity.length;
            if (sum < thr) {
                System.out.printf("Number of hash function to maximum avg distance %5.5s = %s\n", thr, seedSize);
                optimalK = seedSize;
                break;
            }
        }
    }

    static MinHash getMinHash(File file, int seedSize) {
        var shingles = MinHash.shinglesHashCodeFromCharArr((new Book(file)).getContent(), shingleSize);
        Hash.hashSeed = new HashSeed(seedSize);
        return new MinHash(shingles);
    }

    static File[] createFiles(int numFiles) {
        File[] fileArr = new File[numFiles + 1];
        fileArr[0] = new File("Projeto-MPEI" + File.separator + "src" + File.separator + "Tests" + File.separator + "test" + File.separator + "test.txt");
        try (Scanner fileReader = new Scanner(fileArr[0])) {
            String sentence = "";
            while (fileReader.hasNext()) {
                sentence += fileReader.next();
            }
            fileSize = sentence.length();
            fileReader.close();
            double base = Math.log(numFiles);

            for (int i = 1; i <= numFiles; i++) {
                fileArr[i] = new File("Projeto-MPEI" + File.separator + "src" + File.separator + "Tests" + File.separator + "test" + File.separator + "test" + i * 10 + ".txt");
                PrintWriter writeFiles = new PrintWriter(fileArr[i]);
                for (int x = 0; x < fileSize; x++) {
                    writeFiles.print(Math.random() < Math.log(i) / base / 3 + 0.66 ? sentence.charAt(x) : randomChar());

                }
                writeFiles.close();
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return fileArr;
    }

    static char randomChar() {
        Random r = new Random();
        return (char) (r.nextInt(57) + 'A');
    }
}

