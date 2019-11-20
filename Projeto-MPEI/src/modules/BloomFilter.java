package modules;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class BloomFilter {
    private int n, k;
    private boolean[] filter;

    public BloomFilter(int n, int k) {
        this.n = n;
        this.k = k;
        this.filter = new boolean[n];
    }


    public static int optimalK(int n, int m) {
        return optimalK((long) n, (long) m);
    }

    public static int optimalK(long n, long m) {
        int optK = (int) Math.round(n * 0.693 / m);
        return optK == 0 ? 1 : optK;
    }

    public static BloomFilter fromFile(String fName) {
        File book = new File(fName);
        Set<String> wordSet = new HashSet<String>();
        try (Scanner k = new Scanner(book)) {
            while (k.hasNext()) {
                wordSet.add(k.next());
            }
        } catch (Exception e) {
            System.out.println("Runtime Error: " + e.toString());
        }
        var b = new BloomFilter(wordSet.size() * 4, BloomFilter.optimalK(wordSet.size() * 8, wordSet.size()));
        for (var elem : wordSet) {
            b.addElement(elem);
        }
        return b;
    }

    public double probErr(int numElem) {
        return Math.pow(1 - Math.pow(1 - (double) 1 / n, k * numElem), k);
    }

    public void addElement(String elem) {
        var cond = true;
        for (var i = 0; i < k; i++) {
            if (cond) filter[Math.abs((elem + i).hashCode() % n)] = true;
            else filter[string2hash(elem, n, i)] = true;
            cond = !cond;
        }
    }

    public boolean isElement(String elem) {
        var cond = true;
        for (var i = 0; i < k; i++) {
            if (cond && !filter[Math.abs((elem + i).hashCode() % n)]) {
                return false;
            } else if (!cond && !filter[string2hash(elem, n, i)]) {
                return false;
            }
            cond = !cond;
        }
        return true;
    }

    private int string2hash(String s, int mod, int seed) {
        long result = (long) Math.pow(seed, seed * 3);
        char[] ca = s.toCharArray();
        for (var c : ca) {
            result = result * 31 + c;
        }
        result = Math.abs(result);
        return (int) (result % mod);
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
