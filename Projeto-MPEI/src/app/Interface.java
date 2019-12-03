package app;

import Threads.BookDirectoryProcessor;
import modules.BloomFilter;
import modules.CountFilter;
import modules.MinHash;
import util.*;

import java.io.*;
import java.util.*;

import static util.Environment.contentShingleSize;
import static util.Environment.titleShingleSize;

class Interface {
    public static HashMap<String, ProcessedBooksResult> bookStockHashes = new HashMap<>();
    public static BloomFilter availableBooks = new BloomFilter();
    public static CountFilter requestedBooks;

    public static HashMap<Pair<String, String>, Pair<Double, Double>> similarity = new HashMap<>();

    //This function may not download numOfBooks, this argument is just an indication
    public static void downloadTestData(File dir, int numOfBooks, Mutable<Double> progress) {
        Thread bd = new BookDownloader(numOfBooks, dir, progress);
        bd.start();
    }

    public static boolean checkBook(String name) {
        return availableBooks.isElement(name);
    }

    public static boolean addBook(File f) {
        Book b = new Book(f);
        var toAdd = new ProcessedBooksResult();
        toAdd.minHashedContent = new MinHash(MinHash.shinglesHashCodeFromCharArr(b.getContent(), contentShingleSize));
        toAdd.minHashedContent = new MinHash(MinHash.shinglesHashCodeFromCharArr(b.getTitle(), titleShingleSize));
        StringBuilder sb = new StringBuilder();
        for(var c: b.getTitle())
            sb.append(c);
        toAdd.name = sb.toString();
        if (availableBooks.isElement(toAdd.name)) return false;
        availableBooks.addElement(toAdd.name);
        bookStockHashes.put(f.getName(), toAdd);
        return true;
    }

    private static void allSim() {
        TimeThis t = new TimeThis(similarity.keySet().size() > 0 ? "Look Up All Similarities" : "Calc All Similarities", "e");
        for (var key1 : bookStockHashes.keySet()) {
            for (var key2 : bookStockHashes.keySet()) {
                if (!similarity.keySet().contains(new Pair<>(key1, key2))) {
                    TimeThis t1 = new TimeThis("sena", "e");
                    double contentSim = bookStockHashes.get(key1).minHashedContent.calcSimTo(bookStockHashes.get(key2).minHashedContent);
                    t1.end();
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

    public static boolean isAvailable(String name) {
        if (!availableBooks.isElement(name)) return false;
        if (requestedBooks.isElement(name)) return false;
        return true;
    }

    public static boolean requestBook(String name) {
        if (!availableBooks.isElement(name)) return false;
        if (requestedBooks.isElement(name)) return false;
        requestedBooks.addElement(name);
        return true;
    }

    public static boolean returnBook(String name) {
        if (!availableBooks.isElement(name)) return false;
        if (!requestedBooks.isElement(name)) return false;
        requestedBooks.remElement(name);
        return true;
    }

    public static double compareBooks(ProcessedBooksResult b1, ProcessedBooksResult b2) {
        return b1.minHashedContent.calcSimTo(b2.minHashedContent);
    }

    public static HashMap<String, ProcessedBooksResult> getAvailableBooks() {
        return bookStockHashes;
    }

    public static void parseDirectory(File dir, Mutable<Double> progress) {
        BookDirectoryProcessor processor = new BookDirectoryProcessor(dir, progress, availableBooks);
        processor.start();
        bookStockHashes = processor.result;
        while (availableBooks == null || availableBooks.getN() == 0)
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        requestedBooks = new CountFilter(availableBooks.getN(), availableBooks.getK());
        availableBooks.addElement("");
    }

    public static void save(File destination) throws IOException {
        FileOutputStream file = new FileOutputStream(destination);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(new AppState(bookStockHashes, availableBooks, requestedBooks));
        out.close();
        file.close();
    }

    public static void load(File source) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(source);
        ObjectInputStream in = new ObjectInputStream(file);
        AppState tmp = (AppState) in.readObject();
        bookStockHashes = tmp.getBookStockHashes();
        availableBooks = tmp.getAvailableBooks();
        requestedBooks = tmp.getRequestedBooks();
        in.close();
        file.close();
    }


}
