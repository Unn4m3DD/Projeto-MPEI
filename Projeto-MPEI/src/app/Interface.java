package app;

import util.BookDirectoryProcessor;
import util.Mutable;
import util.ProcessedBooksResult;

import java.io.File;
import java.util.HashMap;

class Interface {
    private static HashMap<String, ProcessedBooksResult> currentData = new HashMap<>();
    private static File currentDir = new File("books\\Spanish");

    public static void parseDirectory(Mutable<Double> progress) {
        BookDirectoryProcessor processor = new BookDirectoryProcessor(currentDir, progress);
        processor.start();
        currentData = processor.result;
    }


}
