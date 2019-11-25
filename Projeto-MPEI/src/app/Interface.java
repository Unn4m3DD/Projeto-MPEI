package app;

import util.BookCrawler;
import util.BookDirectoryProcessor;
import util.Mutable;
import util.ProcessedBooksResult;

import java.io.File;
import java.util.HashMap;

class Interface {
    private static File currentDir = new File("books\\Test");

    public static File setCurrentDirectory() {
        return currentDir;
    }

    //This function may not download numOfBooks, this argument is just an indication
    public static void downloadTestData(int numOfBooks) {
        new BookCrawler(numOfBooks, currentDir);
    }

    public static void main(String[] args) {
        downloadTestData(100);
    }

    private static HashMap<String, ProcessedBooksResult> currentData = new HashMap<>();

    public static void parseDirectory(Mutable<Double> progress) {
        BookDirectoryProcessor processor = new BookDirectoryProcessor(currentDir, progress);
        processor.start();
        currentData = processor.result;
    }


}
