package app;

import Threads.BookDirectoryProcessor;
import modules.BloomFilter;
import modules.CountFilter;
import modules.LSH;
import modules.MinHash;
import util.*;

import java.io.*;
import java.util.*;

import static util.Environment.titleShingleSize;

class Interface {
    public static HashMap<String, ProcessedBooksResult> bookStockHashes = new HashMap<>();
    public static BloomFilter availableBooks = new BloomFilter();
    public static CountFilter stock;
    public static HashMap<String, LSH> bookStockFingerprints = new HashMap<>();

    public static HashMap<Pair<String, String>, Pair<Double, Double>> similarity = new HashMap<>();

    //This function may not download numOfBooks, this argument is just an indication
    public static void downloadTestData(File dir,int numOfBooks, Mutable<Double> progress) {
        Thread bd = new BookDownloader(numOfBooks, dir, progress);
        bd.start();
    }

    public static boolean checkBook(String name) {
        return availableBooks.isElement(name);
    }

    private static void generateFingerprint() {
        for (var key : bookStockHashes.keySet()) {
            bookStockFingerprints.put(key, new LSH(bookStockHashes.get(key).minHashedContent, 4));
        }
    }

    private static void allSim() {
        TimeThis t = new TimeThis(similarity.keySet().size() > 0 ? "Look Up All Similarities" : "Calc All Similarities", "e");
        for (var key1 : bookStockHashes.keySet()) {
            for (var key2 : bookStockHashes.keySet()) {
                if (!similarity.keySet().contains(new Pair<>(key1, key2))) {
                    double contentSim = bookStockHashes.get(key1).minHashedContent.calcSimTo(bookStockHashes.get(key2).minHashedContent);
                    double titleSim = bookStockHashes.get(key1).minHashedTitle.calcSimTo(bookStockHashes.get(key2).minHashedTitle);
                    similarity.put(
                            new Pair<>(key1, key2),
                            new Pair<>(contentSim, titleSim));
                    similarity.put(
                            new Pair<>(key2, key1),
                            new Pair<>(contentSim, titleSim));
                }
            }
        }
        t.end();
    }

    public static HashMap<String, List<SimContainer>> allSimContent(double thr) {
        if (similarity.keySet().size() == 0)
            allSim();
        HashMap<String, List<SimContainer>> result = new HashMap<>();
        for (var key1 : bookStockHashes.keySet()) {
            result.put(bookStockHashes.get(key1).name, new LinkedList<>());
            for (var key2 : bookStockHashes.keySet()) {
                double sim = similarity.get(new Pair<>(key1, key2)).elem1;
                if (thr < sim && (!key1.equals(key2))) {
                    result.get(bookStockHashes.get(key1).name).add(new SimContainer(bookStockHashes.get(key2).name, sim));
                }

            }
        }
        return result;
    }

    public static HashMap<String, List<SimContainer>> allSimTitle(double thr) {
        if (similarity.keySet().size() == 0)
            allSim();
        HashMap<String, List<SimContainer>> result = new HashMap<>();
        for (var key1 : bookStockHashes.keySet()) {
            result.put(bookStockHashes.get(key1).name, new LinkedList<>());
            for (var key2 : bookStockHashes.keySet()) {
                double sim = similarity.get(new Pair<>(key1, key2)).elem2;
                if (thr < sim && (!key1.equals(key2))) {
                    result.get(bookStockHashes.get(key1).name).add(new SimContainer(bookStockHashes.get(key2).name, sim));
                }

            }
        }
        return result;
    }

    public static List<ProcessedBooksResult> searchBook(String name, double thr) {
        ArrayList<ProcessedBooksResult> result = new ArrayList<>();
        ArrayList<Character> list = new ArrayList<>();
        for (char c : name.toCharArray()) {
            list.add(c);
        }

        MinHash nameMinHash =
                new MinHash(
                        MinHash.shinglesHashCodeFromCharArr(list, titleShingleSize)
                );
        for (var item : bookStockHashes.values()) {
            if (item.minHashedTitle.calcSimTo(nameMinHash) >= thr) {
                result.add(item);
            }
        }
        return result;
    }

    public static boolean requestBook(String name) {
        if (!availableBooks.isElement(name)) return false;
        if (stock.isElement(name)) return false;
        stock.addElement(name);
        return true;
    }

    public static boolean returnBook(String name) {
        if (!availableBooks.isElement(name)) return false;
        if (!stock.isElement(name)) return false;
        stock.remElement(name);
        return true;
    }

    public static double compareBooks(ProcessedBooksResult b1, ProcessedBooksResult b2) {
        return b1.minHashedContent.calcSimTo(b2.minHashedContent);
    }

    public static HashMap<String, ProcessedBooksResult> getAvailableBooks() {
        return bookStockHashes;
    }

    public static void parseDirectory(File dir,Mutable<Double> progress) {
        BookDirectoryProcessor processor = new BookDirectoryProcessor(dir, progress, availableBooks);
        processor.start();
        bookStockHashes = processor.result;
        while (availableBooks == null)
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        stock = new CountFilter(availableBooks.getN(), availableBooks.getK());

    }

    public static void save(File destination) throws IOException {
        FileOutputStream file = new FileOutputStream(destination);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(new AppState(bookStockHashes, availableBooks, stock));
        out.close();
        file.close();
    }

    public static void load(File source) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(source);
        ObjectInputStream in = new ObjectInputStream(file);
        AppState tmp = (AppState) in.readObject();
        bookStockHashes = tmp.getBookStockHashes();
        availableBooks = tmp.getAvailableBooks();
        stock = tmp.getStock();
        in.close();
        file.close();
    }


}
