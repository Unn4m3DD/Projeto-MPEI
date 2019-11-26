package Tests;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import util.Environment.*;

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
            if ((x ) % (testWeight / 10) == 0)
                System.out.printf((double)x / testWeight + ", ");
            totalError += ((double) err / numOfHashes);
        }
        System.out.println();
        totalError /= testWeight;
        if (totalError > accuracy)
            System.out.println("Erro no teste da função de hash");
        if (totalError > accuracy)
            System.out.println("Passou!");
    }
}
