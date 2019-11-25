package modules;

import util.TimeThis;

import java.io.Serializable;
import java.util.Random;

public class HashSeed implements Serializable {
    int[] a, b;
    int size;

    public HashSeed(int size) {
        var t = new TimeThis("HashSeed Generate");
        this.size = size;
        a = new int[size];
        b = new int[size];
        Random generator = new Random("Seed constante para comparar varias bibliotecas".hashCode());
        for (var i = 0; i < size; i++) {
            a[i] = generator.nextInt();
            b[i] = generator.nextInt();
        }
        t.end();
    }
}
