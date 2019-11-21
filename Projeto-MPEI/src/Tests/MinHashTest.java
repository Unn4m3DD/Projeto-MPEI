package Tests;

import modules.MinHash;
import modules.MinHashSeed;
import util.Book;
import util.TimeThis;

import java.io.*;
import java.util.*;

public class MinHashTest {
    public static FileWriter fw;
    public static File[] listFiles;
    public static MinHashSeed mhs;

    public static void main(String[] args) throws IOException, InterruptedException {
        fw = new FileWriter(new File("results.txt"));
//        File d = new File("books\\TestBase");
        File d = new File("books\\English");
//        File d = new File("books\\Spanish");
        LinkedList<Book> books = new LinkedList<>();
        TimeThis t = new TimeThis("Files to BookList ", "e");
        for (var f : d.listFiles()) {
            books.add(new Book(f));
        }
        t.end();
        listFiles = d.listFiles();
        HashMap<String, MinHash> mhList = new HashMap<>();
        mhList = create();
        save(mhList, mhs);
//        var saved = load();
//        mhList = saved.mh;
//        mhs = saved.mhs;
        ArrayList<String> testedKeys = new ArrayList<>();
        t = new util.TimeThis("Round of comp", "e");
        for (var key1 : mhList.keySet()) {
            testedKeys.add(key1);
            for (var key2 : mhList.keySet()) {
                if(testedKeys.contains(key2))
                    continue;
                var m1 = mhList.get(key1);
                var m2 = mhList.get(key2);
                double sim = m1.calcSimTo(m2);
                try {
                    fw.write(String.format("%10.10s e %10.10s têm similaridade %5.5s\n", key1, key2, sim));
                    if (sim > .9)
                        System.out.println(String.format("%10.10s e %10.10s têm similaridade %5.5s", key1, key2, sim));
                    //fw.flush();
                } catch (Exception e) {
                }
            }
        }
        t.end();
        fw.flush();

    }

    private static HashMap<String, MinHash> create() {
        HashMap<String, MinHash> mhList;
        mhs = new MinHashSeed(150);
        mhList = MinHash.fromFileList(listFiles, mhs, 8);
        return mhList;
    }

    private static ToSave load() throws IOException {
        ToSave result = new ToSave();
        FileInputStream fileIn = new FileInputStream("minHashes.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        try {
            result = (ToSave) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        in.close();
        fileIn.close();
        return result;
    }

    private static void save(HashMap<String, MinHash> mhList, MinHashSeed mhs) throws IOException {
        FileOutputStream fileOut =
                new FileOutputStream("minHashes.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        ToSave toSave = new ToSave();
        toSave.mhs = mhs;
        toSave.mh = mhList;
        out.writeObject(toSave);
        out.close();
        fileOut.close();
    }
}

class ToSave implements Serializable {
    MinHashSeed mhs;
    HashMap<String, MinHash> mh;
}