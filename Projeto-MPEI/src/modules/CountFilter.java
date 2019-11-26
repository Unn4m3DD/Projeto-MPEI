package modules;

import java.io.Serializable;

import static util.Environment.hashSeed;

public class CountFilter implements Serializable {
    private int n, k;
    private int[] filter;

    public CountFilter(int n, int k) {
        this.n = n;
        this.k = k;
        this.filter = new int[n];
    }


    public static int optimalK(int n, int m) {
        return optimalK((long) n, (long) m);
    }

    private static int optimalK(long n, long m) {
        int optK = (int) Math.round(n * 0.693 / m);
        return optK == 0 ? 1 : optK;
    }

    public double probErr(int numElem) {
        return Math.pow(1 - Math.pow(1 - (double) 1 / n, k * numElem), k);
    }

    public void addElement(String elem) {
        for (var i = 0; i < k; i++) {
            filter[hash(elem, i)]++;
        }
    }

    private int hash(String elem, int i) {
        return Math.abs(hashSeed.a[i] * (elem).hashCode() + hashSeed.b[i]) % n;
    }

    public boolean isElement(String elem) {
        for (var i = 0; i < k; i++) {
            if (filter[hash(elem, i)] == 0) {
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

    public int[] getFilter() {
        return filter;
    }
}
