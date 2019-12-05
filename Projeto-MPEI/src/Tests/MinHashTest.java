package Tests;

import modules.Hash;
import modules.HashSeed;
import modules.MinHash;
import util.Book;
import util.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class MinHashTest {
    public static int shingleSize = 8;
    public static int fileSize = 50;
    public static double accuracy = 0.05;
    public static int optimalK = 150;

    public static void main(String[] args) {
        optimalNumberOfHashes(accuracy);
        minHashTest();
    }

    static void minHashTest() {
        System.out.println("Initializing test for minhash quality");
        File[] files = createFiles(fileSize);
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
            System.out.println("Difference between Jaccard index and minhash similarity test passed!");
        else
            System.out.println("Jaccard index and minhash calculations are too far apart");
    }

    static void optimalNumberOfHashes(double thr) {
        System.out.println("Initializing test for optimal number of hashes...");
        File[] files = createFiles(fileSize);
        for (int seedSize = 100; seedSize <= 250; seedSize += 50) {
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
                deleteFiles(files);
                return;
            }
        }
        System.out.printf("For 100 < Number Of Hash Function < 250, there is no value that matches the threshold %5.5s\n", thr);
    }

    private static void deleteFiles(File [] files) {
        for (int i = 1; i <files.length; i++) {
            files[i].delete();
        }
    }

    static MinHash getMinHash(File file, int seedSize) {
        var shingles = MinHash.shinglesHashCodeFromCharArr((new Book(file)).getContent(), shingleSize);
        Hash.hashSeed = new HashSeed(seedSize);
        MinHash result = new MinHash(shingles);
        Hash.hashSeed = new HashSeed(Environment.numberOfHashesForMinHash);
        return result;

    }

    static File[] createFiles(int numFiles) {
        File[] fileArr = new File[numFiles + 1];
        fileArr[0] = new File("test" + File.separator + "test.txt");

//        fileArr[0] = new File("Projeto-MPEI" + File.separator + "src" + File.separator + "Tests" + File.separator + "test" + File.separator + "test.txt");
        try (Scanner fileReader = new Scanner(fileArr[0])) {
            String sentence = "";
            while (fileReader.hasNext()) {
                sentence += fileReader.next();
            }
            fileReader.close();
            double base = Math.log(numFiles);

            for (int i = 1; i <= numFiles; i++) {
               // fileArr[i] = new File("Projeto-MPEI" + File.separator + "src" + File.separator + "Tests" + File.separator + "test" + File.separator + "test" + i * 10 + ".txt");
                fileArr[i] = new File("test" + File.separator + "test" + i + ".txt");
                PrintWriter writeFiles = new PrintWriter(fileArr[i], Charset.forName("UTF-8"));
                for (int x = 0; x < sentence.length(); x++) {
                    writeFiles.print(Math.random() < Math.log(i) / base / 3 + 0.66 ? sentence.charAt(x) : randomChar());

                }
                writeFiles.close();
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return fileArr;
    }

    static char randomChar() {
        Random r = new Random();
        return (char) (r.nextInt(57) + 'A');
    }
}

