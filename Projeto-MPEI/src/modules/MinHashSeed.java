package modules;

import util.TimeThis;

import java.io.Serializable;

public class MinHashSeed implements Serializable {
    int[] a, b;
    int size;

    public MinHashSeed(int size) {
        var t = new TimeThis("MinHashSeed Generate");
        this.size = size;
        a = new int[size];
        b = new int[size];
        for (var i = 0; i < size; i++) {
            a[i] = randAB();
            b[i] = randAB();
        }
        t.end();
    }


    private int randAB() {
        return (int) Math.floor(Math.random() * (Integer.MAX_VALUE - 2)) * (Math.random() > .5 ? -1 : 1);

    }
}
