package Threads;

import Threads.BookContentProcessor;
import Threads.BookTitleProcessor;
import Threads.FileToBookProcessor;
import modules.BloomFilter;
import util.Book;
import util.Mutable;
import util.ProcessedBooksResult;
import util.TimeThis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class BookDirectoryProcessor extends Thread {
    private ConcurrentLinkedQueue<Book>
            toProcessTitle = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Book> toProcessContent = new ConcurrentLinkedQueue<>();
    private Mutable<Boolean> finished = new Mutable<>(false);
    public HashMap<String, ProcessedBooksResult> result = new HashMap<>();
    private File dir;
    private Mutable<Double> progress;
    private BloomFilter availableBooks ;
    public BookDirectoryProcessor(File dir, Mutable<Double> progress, BloomFilter availableBooks) {
        this.dir = dir;
        this.progress = progress;
        this.availableBooks = availableBooks;
    }

    public void run() {
        TimeThis.currentlyTiming = true;
        TimeThis t1 = new TimeThis("Processamento para MinHash e BloomFilter", "e");
        Thread[] threads = new Thread[6];
        threads[0] = new FileToBookProcessor(toProcessTitle, toProcessContent, dir, finished, progress, availableBooks);
        try{
            Thread.sleep(50);
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }
        threads[1] = new BookTitleProcessor(toProcessTitle, toProcessContent, finished, result, availableBooks);
        threads[2] = new BookContentProcessor(toProcessContent, finished, result);
        threads[3] = new BookContentProcessor(toProcessContent, finished, result);
        threads[4] = new BookContentProcessor(toProcessContent, finished, result);
        threads[5] = new BookContentProcessor(toProcessContent, finished, result);
//        threads[6] = new BookContentProcessor(toProcessContent, finished, result, hashSeed);
        for (Thread thread : threads) thread.start();

        while (threads[3].isAlive()) {
            try {
                Thread.sleep(1000);
                TimeThis t = new TimeThis("Garbage collector delay", "v");
                System.gc();
                t.end();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        for (var i = 0; i < threads.length; i++) {
            try {
                threads[i].join();
//                System.out.println("Thread " + i + " joined");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        progress.set(1.0);
        t1.end();
        TimeThis.printAllDelays();

    }

    private static void compareAll(HashMap<String, ProcessedBooksResult> result) {
        ArrayList<String> testedKeys = new ArrayList<>(result.size());
        TimeThis t = new TimeThis("Comparação de " + result.size() + "items", "v");
        for (var key1 : result.keySet()) {
            testedKeys.add(key1);
            for (var key2 : result.keySet()) {
                if (testedKeys.contains(key2))

                    continue;
                var m1 = result.get(key1).minHashedContent;
                var m2 = result.get(key2).minHashedContent;
                double sim = m1.calcSimTo(m2);
                System.out.println(String.format("%10.10s e %10.10s têm similaridade %5.5s", key1, key2, sim));
            }
        }
        t.end();
    }
}
