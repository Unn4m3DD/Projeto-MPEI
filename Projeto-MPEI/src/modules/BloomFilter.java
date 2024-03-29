package modules;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static modules.Hash.hash;

public class BloomFilter implements Serializable {
    private int n, k;
    private boolean[] filter;

    public BloomFilter(int n, int k) {
        this.n = n;
        this.k = k;
        this.filter = new boolean[n];
    }

    public BloomFilter() {
    }

    public void setParams(int n, int k) {
        this.n = n;
        this.k = k;
        this.filter = new boolean[n];
    }

    public static int optimalK(int n, int m) {
        return optimalK((long) n, (long) m);
    }

    private static int optimalK(long n, long m) {
        int optK = (int) Math.round(n * 0.693 / m);
        return optK == 0 ? 1 : optK;
    }

    public static BloomFilter fromFile(String fName, int n) {
        File book = new File(fName);
        Set<String> wordSet = new HashSet<>();
        try (Scanner k = new Scanner(book)) {
            while (k.hasNext()) {
                wordSet.add(k.next());
            }
        } catch (Exception e) {
            System.out.println("Runtime Error: " + e.toString());
        }
        var b = new BloomFilter(wordSet.size() * n, BloomFilter.optimalK(wordSet.size() * n, wordSet.size()));
        for (var elem : wordSet) {
            b.addElement(elem);
        }
        return b;
    }

    public double probErr(int numElem) {
        return Math.pow(1 - Math.pow(1 - (double) 1 / n, k * numElem), k);
    }

    public void addElement(String elem) {
        for (var i = 0; i < k; i++) {
            filter[Math.abs(hash(elem.hashCode(), i)) % n] = true;
        }
    }


    public boolean isElement(String elem) {
        if (filter == null) return false;
        for (var i = 0; i < k; i++) {
            if (!filter[Math.abs(hash(elem.hashCode(), i)) % n]) {
                return false;
            }
        }
        return true;
    }

    public int getN() {
        return n;
    }

    public int getK() {
        return k;
    }

    public boolean[] getFilter() {
        return filter;
    }
}
