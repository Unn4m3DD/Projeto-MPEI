package Threads;

import util.Book;
import util.Mutable;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;


public class FileToBookProcessor extends Thread {
    private ConcurrentLinkedQueue<Book> toProcessTitle;
    private ConcurrentLinkedQueue<Book> toProcessContent;
    private File dir;
    private Mutable<Boolean> finished;
    private Mutable<Double> progress;

    public FileToBookProcessor(
            ConcurrentLinkedQueue<Book> toProcessTitle,
            ConcurrentLinkedQueue<Book> toProcessContent,
            File dir,
            Mutable<Boolean> finished,
            Mutable<Double> progress
    ) {
        this.toProcessTitle = toProcessTitle;
        this.dir = dir;
        this.finished = finished;
        this.toProcessContent = toProcessContent;
        this.progress = progress;
    }

    public void run() {
        if (dir.isDirectory())
            try {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        if (toProcessContent.size() > 20)
                            try {
                                sleep(1000);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        Book b = new Book(files[i]);
                        toProcessTitle.add(b);
                    }
                    if ((i % (files.length / 100) == 0))
                        progress.set((double) i / files.length);
                }
            } catch (NullPointerException e) {
                System.out.println(dir.getName() + " está vazio.");
            }
        finished.set(true);
    }
}