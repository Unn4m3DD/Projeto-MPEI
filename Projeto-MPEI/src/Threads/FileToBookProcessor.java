package Threads;

import util.Book;
import util.MutableBoolean;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileToBookProcessor extends Thread {
    ConcurrentLinkedQueue<Book> toProcessTitle, toProcessContent;
    File dir;
    MutableBoolean finished, shouldSleep;

    public FileToBookProcessor(ConcurrentLinkedQueue<Book> toProcessTitle, ConcurrentLinkedQueue<Book> toProcessContent, File dir, MutableBoolean finished, MutableBoolean shouldSleep) {
        this.toProcessTitle = toProcessTitle;
        this.dir = dir;
        this.finished = finished;
        this.shouldSleep = shouldSleep;
        this.toProcessContent = toProcessContent;
    }

    public void run() {
        if (dir.isDirectory())
            try {
                for (File file : dir.listFiles()) {
                    if (file.isFile()) {
                        if (toProcessContent.size() > 20)
                            try {
                                sleep(1000);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        Book b = new Book(file);
                        toProcessTitle.add(b);
                        if (shouldSleep.getB()) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                    }
                }
            } catch (NullPointerException e) {
                System.out.println(dir.getName() + " está vazio.");
            }
        finished.setB(true);
    }
}
