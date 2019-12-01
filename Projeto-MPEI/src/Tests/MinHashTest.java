package Tests;

import modules.Hash;
import modules.HashSeed;
import modules.MinHash;
import util.Book;
import util.TimeThis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

class MinHashTest {
    public static int shingleSize = 8;
    public static int fileSize;
    public double accuracy = 0.05;

    public static void main(String[] args) throws FileNotFoundException {
        testSimilarity();
    }

    private static void testSimilarity() throws FileNotFoundException {
        File[] files = createFiles(100);
        for (int seedSize = 100; seedSize <= 250; seedSize += 50) {
            System.out.println("Seed size: " + seedSize);
            double[] similarity = new double[files.length - 1];
            MinHash originalfile = getMinHash(files[0], seedSize);
            TimeThis t4 = new TimeThis("Calc all minhash");
            for (int i = 0; i < similarity.length; i++) {
                TimeThis t3 = new TimeThis("Calc single minhash");
                similarity[i] = originalfile.calcSimTo(getMinHash(files[i + 1], seedSize));
                t3.end();
            }
            t4.end();

            double[] jaccardIndex = new double[similarity.length];
            TimeThis t2 = new TimeThis("Calc all jaccard");
            for (int i = 0; i < jaccardIndex.length; i++) {
                TimeThis t = new TimeThis("Calc single jaccard");
                jaccardIndex[i] = MinHash.jaccardIndex(files[0], files[i + 1], shingleSize);
                t.end();
            }
            t2.end();

            TimeThis.printAllDelays();
            double sum = 0;
            for (int i = 0; i < similarity.length; i++) {
                sum+=Math.abs(similarity[i]- jaccardIndex[i]);
            }
            sum/= similarity.length;
            System.out.println(sum);
        }
    }

    private static MinHash getMinHash(File file, int seedSize) {
        var shingles = MinHash.shinglesHashCodeFromCharArr((new Book(file)).getContent(), shingleSize);
        Hash.hashSeed = new HashSeed(seedSize);
        return new MinHash(shingles);
    }

    private static File[] createFiles(int numFiles) throws FileNotFoundException {
        TimeThis t = new TimeThis("Create Files");
        File[] fileArr = new File[numFiles + 1];
        fileArr[0] = new File("Projeto-MPEI" + File.separator + "src" + File.separator + "Tests" + File.separator + "test" + File.separator + "test.txt");
        Scanner fileReader = new Scanner(fileArr[0]);
        String sentence = "";
        while (fileReader.hasNext()) {
            sentence += fileReader.next();
        }
        fileSize = sentence.length();
        fileReader.close();
        double base = Math.log(numFiles);

        for (int i = 1; i <= numFiles; i++) {
            TimeThis t2 = new TimeThis("Create Single File");
            fileArr[i] = new File("Projeto-MPEI" + File.separator + "src" + File.separator + "Tests" + File.separator + "test" + File.separator + "test" + i * 10 + ".txt");
            PrintWriter writeFiles = new PrintWriter(fileArr[i]);
            for (int x = 0; x < fileSize; x++) {
                writeFiles.print(Math.random() < Math.log(i) / base / 3 + 0.66 ? sentence.charAt(x) : randomChar());

            }
            writeFiles.close();
            t2.end();
        }
        t.end();
        return fileArr;
    }

    private static char randomChar() {
        Random r = new Random();
        return (char) (r.nextInt(57) + 'A');
    }
}

