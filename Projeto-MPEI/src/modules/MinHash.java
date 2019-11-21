package modules;

import util.Book;
import util.TimeThis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinHash implements Serializable {
    int[] signature;
    MinHashSeed mhs;

    public MinHash(List<String> set, MinHashSeed mhs) {
        this.mhs = mhs;
        TimeThis t = new TimeThis("MinHash", "v");
        signature = new int[mhs.size];
        for (var i = 0; i < mhs.size; i++) {
            int min = Integer.MAX_VALUE;
            for (var idx = 0; idx < set.size(); idx++) {
                int c_hash = (mhs.a[i] * set.get(idx).hashCode() + mhs.b[i]);
                if (c_hash < min) min = c_hash;
            }
            signature[i] = min;
        }
        t.end();
    }

    public static HashMap<String, MinHash> fromFileList(File[] listFiles, MinHashSeed mhs, int shingleSize) {
        TimeThis t = new TimeThis("Minhash from file list ", "v");
        HashMap<String, MinHash> result = new HashMap<String, MinHash>();
        int threadCount = 4;
        Thread[] ts = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int min_idx = i * listFiles.length / threadCount;
            int max_idx = (i + 1) * listFiles.length / threadCount;
            ts[i] = new FromFileListHelper(shingleSize, min_idx, max_idx, listFiles, mhs, result);
            ts[i].start();
        }
        for (int i = 0; i < threadCount; i++) {
            try {
                ts[i].join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        System.out.println("For " + listFiles.length + " files");
        t.end();
        return result;
    }



    public double calcSimTo(MinHash m) {
        TimeThis t = new TimeThis("CalcSim", "vv");
        int counter = 0;
        for (var i = 0; i < signature.length; i++) {
            if (signature[i] == m.signature[i]) counter++;
        }
        t.end();
        return (double) counter / (double) signature.length;
    }

    public static List<String> shinglesFromCharArr(ArrayList<Character> charArr, int size) {
        TimeThis t = new TimeThis("Shingle Creation", "v");
        ArrayList<String> result = new ArrayList<>();
        for (var i = 0; i + size < charArr.size(); i++) {
            result.add("");
            for (var j = 0; j < size; j++) {
                result.set(i, result.get(i) + charArr.get(i + j));
            }
        }
        t.end();
        return result;
    }

    public static List<String> shinglesFromFile_old(File f, int size) {
        TimeThis t = new TimeThis("Shingle Creation");
        ArrayList<String> result = new ArrayList<>();
        int c_pos = 0;
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            while (raf.length() > c_pos + size) {
                raf.seek(c_pos);
                byte[] b = new byte[size];
                raf.read(b, 0, size);
                result.add(new String(b, "UTF-8"));
                c_pos++;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        t.end();
        return result;
    }
}

class FromFileListHelper extends Thread {
    int shingleSize, min_idx, max_idx;
    File[] listFiles;
    MinHashSeed mhs;
    HashMap<String, MinHash> result;

    public FromFileListHelper(int shingleSize, int min_idx, int max_idx, File[] listFiles, MinHashSeed mhs, HashMap<String, MinHash> result) {
        this.shingleSize = shingleSize;
        this.min_idx = min_idx;
        this.max_idx = max_idx;
        this.listFiles = listFiles;
        this.mhs = mhs;
        this.result = result;
    }

    public void run() {
        for (int i = min_idx; i < max_idx; i++) {
            result.put(listFiles[i].getName(), new modules.MinHash(modules.MinHash.shinglesFromCharArr(
                    (new Book(listFiles[i]).getContent())
                    , shingleSize), mhs));
//            if (((i - min_idx) % ((max_idx - min_idx) / 1)) == 0) {
//                System.out.println((double) (i - min_idx) / (max_idx - min_idx));
//            }
        }
    }

}

