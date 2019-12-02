package modules;

import util.Book;
import util.TimeThis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

import static modules.Hash.hash;
import static modules.Hash.hashSeed;

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

    public static double jaccardIndex(File file1, File file2, int shingleSize) {
        var characterList1 = shinglesFromCharArray((new Book(file1)).getContent(), shingleSize);
        var characterList2 = shinglesFromCharArray((new Book(file2)).getContent(), shingleSize);

        HashSet<String> union = new HashSet<>();
        union.addAll(characterList1);
        union.addAll(characterList2);
        HashSet<String> set1 = new HashSet<>();
        set1.addAll(characterList1);
        HashSet<String> set2 = new HashSet<>();
        set2.addAll(characterList2);
        set2.retainAll(set1);
        HashSet<String> intersect = set2;

        return (double) intersect.size() / union.size();
    }

    private static int union(String sentence1, String sentence2) {
        Set<Character> sentenceSet = new TreeSet<>();
        for (int i = 0; i < sentence1.length(); i++) {
            sentenceSet.add(sentence1.charAt(i));
        }

        for (int i = 0; i < sentence2.length(); i++) {
            sentenceSet.add(sentence2.charAt(i));
        }

        return sentenceSet.size();

    }

    private static int sameCharacters(String sentence1, String sentence2) {
        Set<Character> sentence1Set = new TreeSet<>();
        for (int i = 0; i < sentence1.length(); i++) {
            sentence1Set.add(sentence1.charAt(i));
        }

        Set<Character> sentence2Set = new TreeSet<>();
        for (int i = 0; i < sentence2.length(); i++) {
            sentence2Set.add(sentence2.charAt(i));
        }
        sentence1Set.retainAll(sentence2Set);
        return sentence1Set.size();
    }


    public double calcSimTo(MinHash m) {
        int counter = 0;
        for (var i = 0; i < signature.length; i++) {
            if (signature[i] != m.signature[i]) counter++;
        }
        return 1 - (double) counter / (double) signature.length;
    }

    public static List<Integer> shinglesHashCodeFromCharArr(ArrayList<Character> charArr, int size) {
        TimeThis t = new TimeThis("Shingle Creation", "v");
        ArrayList<Integer> result = new ArrayList<>(charArr.size());
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

    public static List<String> shinglesFromCharArray(ArrayList<Character> charArr, int size) {
        TimeThis t = new TimeThis("Shingle Creation", "v");
        ArrayList<String> result = new ArrayList<>(charArr.size());
//        ArrayList<Integer> result = new ArrayList<>();
        for (var i = 0; i + size < charArr.size(); i++) {
            StringBuilder toAdd = new StringBuilder();
            for (var j = 0; j < size; j++) {
                toAdd.append(charArr.get(i + j));
            }
            result.add(toAdd.toString());
        }
        t.end();
        return result;
    }

    public int[] getSignature() {
        return signature;
    }
}


