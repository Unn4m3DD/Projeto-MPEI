package modules;

import util.Environment;
import util.TimeThis;

import java.io.*;
import java.util.*;

import static modules.Hash.hash;
import static modules.Hash.hashSeed;
import static util.Environment.*;

public class MinHash implements Serializable {
    private int[] signature;

    public MinHash(List<Integer> set) {
        TimeThis t;
        if (set.size() < 100)
            t = new TimeThis("MinHash Titulo", "v");
        else
            t = new TimeThis("MinHash Conteudo", "v");
        signature = new int[hashSeed.size];
        for (var i = 0; i < hashSeed.size; i++) {
            int min = Integer.MAX_VALUE;
            for (var item : set) {
                int c_hash = hash(item, i);
                if (c_hash < min) min = c_hash;
            }
            signature[i] = min;
        }
        t.end();
    }


    public double calcSimTo(MinHash m) {
        TimeThis t = new TimeThis("CalcSim", "vv");
        int counter = 0;
        for (var i = 0; i < signature.length; i++) {
            if (signature[i] != m.signature[i]) counter++;
        }
        t.end();
        return 1 - (double) counter / (double) signature.length;
    }

    public static List<Integer> shinglesFromCharArr(ArrayList<Character> charArr, int size) {
        TimeThis t = new TimeThis("Shingle Creation", "v");
        ArrayList<Integer> result = new ArrayList<>(charArr.size());
//        ArrayList<Integer> result = new ArrayList<>();
        for (var i = 0; i + size < charArr.size(); i++) {
            StringBuilder toAdd = new StringBuilder();
            for (var j = 0; j < size; j++) {
                toAdd.append(charArr.get(i + j));
            }
            result.add(toAdd.toString().hashCode());
        }
        t.end();
        return result;
    }

    public int[] getSignature() {
        return signature;
    }
}


