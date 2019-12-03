package Tests;

import java.util.HashSet;
import java.util.Random;

import static modules.Hash.hash;
import static util.Environment.numberOfHashesForMinHash;


public class HashTest {
    private static int testWeight = 1000000;
    private static double accuracy = 0.001;

    /*
     This module tests if the hash function maps 2 similar values to different numbers
     and if different hash functions map the same value do distinct number
    */
    public static void main(String[] args) {
        hashTest();
        dispTest();
    }

    public static void hashTest() {
        System.out.printf("Hash function test in execution ");
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
                for (int j = 0; j < numberOfHashesForMinHash; j++) {
                    hashes[i].add(hash(ns[i], j));
                }
            }
            double err = 0;
            for (int i = 0; i < ns.length; i++) {
                if (hashes[i].size() != numberOfHashesForMinHash) err++;
            }
                if ((x) % (testWeight / 10) == 0)
                    System.out.printf((double) x / testWeight + ", ");
            totalError += ((double) err / numberOfHashesForMinHash);
        }
        System.out.println();
        totalError /= testWeight;
        if (totalError > accuracy)
            System.out.println("Error in hash function");
        if (totalError < accuracy)
            System.out.println("Passed !");
    }

    public static void dispTest() {
        int values = 200;//Integer.MAX_VALUE / 1;
        System.out.println("Test for hash dispersion in execution ");
        int numberOfDivisions = (int) Math.pow(2, 16);
        int divisionSize = 2 * (Integer.MAX_VALUE / numberOfDivisions);
        var divisions = new int[numberOfDivisions];
        Random generator = new Random();
        for (int j = 0; j < 10; j++) {
//            System.out.println("Starting distribution test for hash number "+(j+1));
            int hashNumber = (int) 3;//(Math.random() * numberOfHashesForMinHash);
            for (int i = 0; i < values; i++) {
                int hash = (hash(generator.nextInt(), hashNumber));
                for (int k = 0; k < divisions.length; k++) {
                    if (hash <= (Integer.MIN_VALUE + ((k + 1) * divisionSize))) {
                        divisions[k]++;
                        break;
                    }
                }
                if ((i) % (values / 10) == 0)
                    System.out.printf((double) i / values + ", ");
            }

            double avg = 0;

            for (int i = 0; i < numberOfDivisions; i++) {
                avg += ((double) divisions[i] / numberOfDivisions);
            }

            long variance = 0;
            for (int i = 0; i < divisions.length; i++) {
                variance += (Math.pow(divisions[i] - avg, 2) / divisions.length);
            }
            int stdDev = (int) Math.sqrt((double) variance);
            double dist = (double) stdDev / values;
//            System.out.println("Distribução: " + dist);
            double threshold = accuracy;
            if (dist < threshold)
                System.out.println("Passed !");
            else
                System.out.println("Hash was poorly distributed");
        }
    }
}
