package app;

import Threads.BookTitleProcessor;
import util.BookDirectoryProcessor;
import util.Mutable;
import util.ProcessedBooksResult;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;

public class Interface {
    public static HashMap<String, ProcessedBooksResult> currentData;
    public static File currentDir = new File("books\\Spanish");

    public static void parseDirectory(Mutable<Double> progress) {
        BookDirectoryProcessor processor = new BookDirectoryProcessor(currentDir, progress);
        processor.start();
        currentData = processor.result;
    }


}
