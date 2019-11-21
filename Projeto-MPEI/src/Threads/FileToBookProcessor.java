package Threads;

import util.Book;
import util.MutableBoolean;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileToBookProcessor extends Thread {
    ConcurrentLinkedQueue<Book> toProcessTitle, toProcessContent;
    File dir;
    MutableBoolean finished;

    public FileToBookProcessor(ConcurrentLinkedQueue<Book> toProcessTitle, ConcurrentLinkedQueue<Book> toProcessContent, File dir, MutableBoolean finished) {
        this.toProcessTitle = toProcessTitle;
        this.dir = dir;
        this.finished = finished;
        this.toProcessContent = toProcessContent;
    }

    public void run() {
        if (dir.isDirectory())
            try {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        if (toProcessContent.size() > 150)
                            try {
                                sleep(1000);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        Book b = new Book(files[i]);
                        toProcessTitle.add(b);
                    }
                    if((i % (files.length / 100) == 0)) System.out.println((double)i / files.length);
                }
            } catch (NullPointerException e) {
                System.out.println(dir.getName() + " está vazio.");
            }
        finished.setB(true);
    }
}
