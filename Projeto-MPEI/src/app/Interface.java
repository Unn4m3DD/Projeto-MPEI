package app;

import Threads.BookDirectoryProcessor;
import modules.BloomFilter;
import modules.CountFilter;
import modules.MinHash;
import util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static util.Environment.titleShingleSize;

class Interface {
    private static File currentDir = new File("books" + File.separator + "Spanish");
    private static HashMap<String, ProcessedBooksResult> bookStockHashes = new HashMap<>();
    public static BloomFilter availableBooks;
    public static CountFilter stock;

    public static File setCurrentDirectory() {
        return currentDir;
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
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        save(new File("savetest.ser"));
        load(new File("savetest.ser"));
        System.out.println(searchBook("Merodeadores ", .1));
        System.out.println(bookStockHashes);
        System.out.println(checkBook("Los majos de Cádiz"));
        System.out.println(checkBook("Los pescadores de Trépang"));
    }

    public static boolean checkBook(String name) {
        return availableBooks.isElement(name);
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
            if (item.minHashedTitle.calcSimTo(nameMinHash) > thr) {
                result.add(item);
            }
        }
        return result;
    }

    public static boolean requestBook(String name){
        if(!availableBooks.isElement(name)) return false;
        if(stock.isElement(name)) return  false;
        stock.addElement(name);
        return true;
    }
    public static boolean returnBook(String name){
        if(!availableBooks.isElement(name)) return false;
        if(!stock.isElement(name)) return  false;
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
