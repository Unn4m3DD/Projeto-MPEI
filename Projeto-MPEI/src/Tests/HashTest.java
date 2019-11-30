package Tests;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import static modules.Hash.hash;
import static util.Environment.numberOfHashesForMinHash;


public class HashTest {
    private static int numOfHashes = numberOfHashesForMinHash, testWeight = 1000000;
    private static double accuracy = 0.001;

    /*
     This module tests if the hash function maps 2 similar values to different numbers
     and if different hash functions map the same value do distinct number
    */
    public static void main(String[] args) {
        //test1();
        test2();
    }

    public static void test1() {
        System.out.printf("Iniciando teste das funções de hash ");
        double totalError = 0;
        for (int x = 1; x < testWeight; x++) {
            Random r = new Random();
            int[] ns = new int[6];
            ns[0] = r.nextInt();
            ns[1] = ns[0] + 1;
            ns[3] = ns[0] - 1;
            ns[4] = ns[0] + 2;
            ns[5] = ns[0] - 2;
            HashSet<Integer>[] hashes = new HashSet[6];
            for (int i = 0; i < ns.length; i++) {
                hashes[i] = new HashSet<>();
                for (int j = 0; j < numOfHashes; j++) {
                    hashes[i].add(hash(ns[i], j));
                }
            }
            double err = 0;
            for (int i = 0; i < ns.length; i++) {
                if (hashes[i].size() != numOfHashes) err++;
            }
            if ((x) % (testWeight / 10) == 0)
                System.out.printf((double) x / testWeight + ", ");
            totalError += ((double) err / numOfHashes);
        }
        System.out.println();
        totalError /= testWeight;
        if (totalError > accuracy)
            System.out.println("Erro no teste da função de hash");
        if (totalError > accuracy)
            System.out.println("Passou!");
    }

    public static void test2() {
        //TODO revamp this whole mess
        int values = Integer.MAX_VALUE / 100;
        int leftBound = Integer.MIN_VALUE/100;  //numbers were changed because of overflow problems
        int rightBound = Integer.MAX_VALUE/100;
        //long totalSize = rightBound + Math.abs(leftBound);
        int maxElementsSubSections = 100000;
        long numberOfDivisions = (long) Math.ceil(values / maxElementsSubSections) +1;  //if i take this +1 out this wont work
        var divisions = new int[(int) numberOfDivisions][maxElementsSubSections];
        var hashes = new int[values];
        int div = 0;
        int sub = 0;
        for (int i = 0; i < values; i++) {
            Random generator = new Random();
            var hash = (hash(generator.nextInt(), 3));
            //hashes[i] = hash;
            if (sub < maxElementsSubSections) {
                divisions[div][sub] = hash;
                sub++;
            } else if (sub == maxElementsSubSections) {
                sub = 0;
                div++;
            }
        }
        int[] mean = new int[(int) numberOfDivisions];
        for (int i = 0; i < numberOfDivisions; i++) {
            int sum = 0;
            for (int j = 0; j < maxElementsSubSections; j++) {
                sum += divisions[i][j];
            }
            mean[i] = (int) (sum / maxElementsSubSections);
        }
        int[][] minMax = new int[(int) numberOfDivisions][2];
        for (int i = 0; i < numberOfDivisions; i++) {
            int min = (int) divisions[i][0];
            int max = (int) divisions[i][0];
            for (int j = 1; j < maxElementsSubSections; j++) {
                if (divisions[i][j] > max) {
                    max = (int) divisions[i][j];
                }
                if (divisions[i][j] < min) {
                    min = (int) divisions[i][j];
                }
            }
            minMax[i][0] = min;
            minMax[i][1] = max;
        }

        var difference = new int[(int) numberOfDivisions];
        for (int i = 0; i < numberOfDivisions; i++) {
            difference[i] = minMax[i][1] - minMax[i][0]; // max - min -> final - inicial
        }

        var divergences = new int[difference.length];
        for (int i = 0; i < divergences.length; i++) {
            divergences[i] = difference[i]/ mean[i];
        }

        System.out.println(Arrays.toString(divergences));
    }
}
