package Tests;

import modules.Hash;
import modules.HashSeed;
import modules.MinHash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class MinHashTest {
    public static int shingleSize = 8;
    public static int fileSize;

    public static void main(String[] args) throws FileNotFoundException {
        testSimilarity();
    }

    private static void testSimilarity() throws FileNotFoundException {
        File[] files = createFiles();
        for (int seedSize = 100; seedSize <= 250; seedSize += 50) {
            System.out.println("Seed size: " + seedSize);
            double[] similarity = new double[files.length - 1];
            MinHash originalfile = getMinHash(files[0], seedSize);
            for (int i = 0; i < similarity.length; i++) {
                similarity[i] = originalfile.calcSimTo(getMinHash(files[i + 1], seedSize)) * 100;
            }

            //TODO do the same but with jaccardindex
            double[] jaccardIndex = new double[similarity.length];
            for (int i = 0; i < jaccardIndex.length; i++) {
                jaccardIndex[i] = MinHash.jaccardIndex(files[0], files[i + 1]) * 100;
            }
            for (int i = 0; i < similarity.length; i++) {
                System.out.println("Similarity between first and " + (i + 1) + "th file: " + similarity[i] + "|| Jaccard index: " + jaccardIndex[i]);
            }
        }
    }

    private static MinHash getMinHash(File file, int seedSize) throws FileNotFoundException {
        Scanner fileReader = new Scanner(file);
        String sentence = "";
        while (fileReader.hasNext()) {
            sentence += fileReader.next();
        }
        fileReader.close();
        var charArray = sentence.toCharArray();
        ArrayList<Character> charList = new ArrayList<>();
        for (int i = 0; i < charArray.length; i++) {
            charList.add(charArray[i]);
        }
        fileReader.close();
        var shingles = MinHash.shinglesFromCharArr(charList, shingleSize);
        Hash.hashSeed = new HashSeed(seedSize);  //entre 100 e 250
        return new MinHash(shingles);
    }

    private static File[] createFiles() throws FileNotFoundException {
        File[] fileArr = new File[11];
        fileArr[0] = new File("Projeto-MPEI" + File.separator + "src" + File.separator + "Tests" + File.separator + "test" + File.separator + "test.txt");
        Scanner fileReader = new Scanner(fileArr[0]);
        String sentence = "";
        while (fileReader.hasNext()) {
            sentence += fileReader.next();
        }
        fileSize = sentence.length();
        fileReader.close();
        PrintWriter rewrite = new PrintWriter(fileArr[0]);
        rewrite.write(sentence);   //so the original file doesn't have blank spaces
        rewrite.close();

        for (int i = 1; i <= 10; i++) {
            fileArr[i] = new File("Projeto-MPEI" + File.separator + "src" + File.separator + "Tests" + File.separator + "test" + File.separator + "test" + i * 10 + ".txt");
            PrintWriter writeFiles = new PrintWriter(fileArr[i]);
            for (int x = 0; x < fileSize; x++) {
                writeFiles.print(Math.random() * 100 < i * 10 ? sentence.charAt(x) : randomChar());
            }
            writeFiles.close();
        }
        //file index * 10 equals the degree of similarity with the first element of the file array
        return fileArr;
    }

    private static char randomChar() {
        Random r = new Random();
        return (char) (r.nextInt(57) + 'A');
    }
}

