package modules;

import java.io.Serializable;

import static modules.Hash.hash;


public class CountFilter implements Serializable {
    private int n, k;
    private int[] filter;

    public CountFilter(int n, int k) {
        this.n = n;
        this.k = k;
        this.filter = new int[n];
    }

    public void addElement(String elem) {
        for (var i = 0; i < k; i++) {
            filter[Math.abs(hash(elem.hashCode(), i)) % n]++;
        }
    }


    public boolean isElement(String elem) {
        for (var i = 0; i < k; i++) {
            if (filter[Math.abs(hash(elem.hashCode(), i)) % n] == 0) {
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

    public void remElement(String elem) {
        for (var i = 0; i < k; i++) {
            filter[Math.abs(hash(elem.hashCode(), i)) % n]--;
        }
    }
}
