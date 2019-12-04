package app;

import Threads.BookDirectoryProcessor;
import modules.BloomFilter;
import modules.CountFilter;
import modules.LSHVariant;
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
        toAdd.minHashedTitle = new MinHash(MinHash.shinglesHashCodeFromCharArr(b.getTitle(), titleShingleSize));
        StringBuilder sb = new StringBuilder();
        for (var c : b.getTitle())
            sb.append(c);
        toAdd.name = sb.toString();
        if (availableBooks.isElement(toAdd.name)) return false;
        availableBooks.addElement(toAdd.name);
        bookStockHashes.put(f.getName(), toAdd);
        if (similarity.size() > 0) {
            for (var key1 : bookStockHashes.keySet()) {
                if (!similarity.keySet().contains(new Pair<>(key1, f.getName()))) {
                    double contentSim = bookStockHashes.get(key1).minHashedContent.calcSimTo(bookStockHashes.get(f.getName()).minHashedContent);
                    double titleSim = bookStockHashes.get(key1).minHashedTitle.calcSimTo(bookStockHashes.get(f.getName()).minHashedTitle);
                    similarity.put(
                            new Pair<>(key1, f.getName()),
                            new Pair<>(contentSim, titleSim));
                    similarity.put(
                            new Pair<>(f.getName(), key1),
                            new Pair<>(contentSim, titleSim));
                }
            }
        }
        return true;
    }

    private static void allSim() {
        int count = 0;
        if (bookStockHashes.size() < 2000) {
            TimeThis t = new TimeThis(similarity.keySet().size() > 0 ? "Look Up All Similarities" : "Calc All Similarities", "e");
            double pace = bookStockHashes.size() / 100;
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
                if (count++ % pace == 0)
                    System.out.println((double) count / bookStockHashes.size());
            }
            t.end();
        } else {
            HashMap<String, String> candidatePair = new HashMap<>();
            ArrayList<LSHVariant> contentList = new ArrayList<>(bookStockHashes.size());
            ArrayList<LSHVariant> titleList = new ArrayList<>(bookStockHashes.size());
            bookStockHashes.keySet().forEach((e) -> {
                contentList.add(new LSHVariant(bookStockHashes.get(e).minHashedContent, 10, e));
                titleList.add(new LSHVariant(bookStockHashes.get(e).minHashedTitle, 10, e));
            });
            for (LSHVariant lshVariant : contentList) {
                for (LSHVariant lshVariant1 : contentList) {
                    if (lshVariant.isCandidate(lshVariant1, 0.8))
                        candidatePair.put(lshVariant.name, lshVariant1.name);
                }
            }

            for (String key : candidatePair.keySet()) {
                if (!similarity.keySet().contains(new Pair<>(key, candidatePair.get(key)))) {
                    double contentSim = bookStockHashes.get(key).minHashedContent.calcSimTo(bookStockHashes.get(candidatePair.get(key)).minHashedContent);
                    double titleSim = bookStockHashes.get(key).minHashedTitle.calcSimTo(bookStockHashes.get(candidatePair.get(key)).minHashedTitle);
                    similarity.put(
                            new Pair<>(key, candidatePair.get(key)),
                            new Pair<>(contentSim, titleSim));
                    similarity.put(
                            new Pair<>(candidatePair.get(key), key),
                            new Pair<>(contentSim, titleSim));
                }
            }

        }
    }

    public static HashMap<String, List<SimContainer>> allSimContent(double thr) {
        if (similarity.keySet().size() == 0)
            allSim();
        HashMap<String, List<SimContainer>> result = new HashMap<>();
        for (var key1 : bookStockHashes.keySet()) {
            result.put(bookStockHashes.get(key1).name, new LinkedList<>());
            for (var key2 : bookStockHashes.keySet()) {
                if (similarity.get(new Pair<>(key1, key2)) != null) {
                    double sim = similarity.get(new Pair<>(key1, key2)).elem1;
                    if (thr < sim && (!key1.equals(key2))) {
                        result.get(bookStockHashes.get(key1).name).add(new SimContainer(bookStockHashes.get(key2).name, sim));
                    }
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
                if (similarity.get(new Pair<>(key1, key2)) != null) {
                    double sim = similarity.get(new Pair<>(key1, key2)).elem2;
                    if (thr < sim && (!key1.equals(key2))) {
                        result.get(bookStockHashes.get(key1).name).add(new SimContainer(bookStockHashes.get(key2).name, sim));
                    }
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
        out.writeObject(new AppState(bookStockHashes, availableBooks, requestedBooks, similarity));
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
        similarity = tmp.getSimilarity();
        in.close();
        file.close();
    }


}
