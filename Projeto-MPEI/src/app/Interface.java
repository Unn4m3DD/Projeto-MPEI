package app;

import Threads.BookDirectoryProcessor;
import modules.BloomFilter;
import modules.CountFilter;
import modules.LSH;
import modules.MinHash;
import util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static util.Environment.titleShingleSize;

class Interface {
    private static File currentDir = new File("books" + File.separator + "English");
    public static HashMap<String, ProcessedBooksResult> bookStockHashes = new HashMap<>();
    public static BloomFilter availableBooks = new BloomFilter();
    public static CountFilter stock;
    public static HashMap<String, LSH> bookStockFingerprints = new HashMap<>();

    public static File getCurrentDirectory() {
        return currentDir;
    }
    public static File setCurrentDirectory(File dir) {
        return currentDir = dir;
    }

    //This function may not download numOfBooks, this argument is just an indication
    public static void downloadTestData(int numOfBooks) {
        System.err.println("não te esqueças de indicar que o argumento é meramente indicativo");
        new BookDownloader(numOfBooks, currentDir);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Mutable<Double> prog = new Mutable<>(0.0);
        parseDirectory(prog);
        while (!prog.get().equals(1.0))
            try {
                Thread.sleep(40 * 60 * 1000 / 100);
                System.out.println(prog.get());
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        save(new File("english.ser"));
//        load(new File("savetest.ser"));
        System.out.println(allSim());

    }

    public static boolean checkBook(String name) {
        return availableBooks.isElement(name);
    }

    private static void generateFingerprint() {
        for (var key : bookStockHashes.keySet()) {
            bookStockFingerprints.put(key, new LSH(bookStockHashes.get(key).minHashedContent, 4));
        }
    }

    public static HashMap<ProcessedBooksResult, List<ProcessedBooksResult>> allSim() {
        // TODO: 27/11/2019 Refazer isto pq está só estupido
//        generateFingerprint();
        HashMap<ProcessedBooksResult, List<ProcessedBooksResult>> result = new HashMap<>();
        for (var key1 : bookStockHashes.keySet()) {
            result.put(bookStockHashes.get(key1), new LinkedList<>());
            for (var key2 : bookStockHashes.keySet()) {
                if (!result.keySet().contains(key2))
                    if (0.4 < bookStockHashes.get(key1).minHashedContent.calcSimTo(bookStockHashes.get(key2).minHashedContent) && (!key1.equals(key2))) {
                        result.get(bookStockHashes.get(key1)).add(bookStockHashes.get(key2));
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
                        MinHash.shinglesFromCharArr(list, titleShingleSize)
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

    public static void parseDirectory(Mutable<Double> progress) {
        BookDirectoryProcessor processor = new BookDirectoryProcessor(currentDir, progress, availableBooks);
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
