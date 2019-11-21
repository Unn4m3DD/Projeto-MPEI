package util;

import Threads.BookContentProcessor;
import Threads.BookTitleProcessor;
import Threads.FileToBookProcessor;
import modules.MinHash;
import modules.MinHashSeed;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.Enviroment.numberOfHashesForMinHash;


public class BookDirectoryProcessor {
    ConcurrentLinkedQueue<Book>
            toProcessTitle = new ConcurrentLinkedQueue<>(),
            toProcessContent = new ConcurrentLinkedQueue<>();
    Mutable<Boolean> finished = new Mutable<Boolean>(false);
    public HashMap<String, ProcessedBooksResult> result = new HashMap<>();
    public BookDirectoryProcessor(File dir, Mutable<Double> progress) {
        MinHashSeed mhs = new MinHashSeed(numberOfHashesForMinHash);
        Thread[] threads = new Thread[6];
        threads[0] = new FileToBookProcessor(toProcessTitle, toProcessContent, dir, finished, progress);
        threads[1] = new BookTitleProcessor(toProcessTitle, toProcessContent, finished, result, mhs);
        threads[2] = new BookContentProcessor(toProcessContent, finished, result, mhs);
        threads[3] = new BookContentProcessor(toProcessContent, finished, result, mhs);
        threads[4] = new BookContentProcessor(toProcessContent, finished, result, mhs);
        threads[5] = new BookContentProcessor(toProcessContent, finished, result, mhs);
//        threads[6] = new BookContentProcessor(toProcessContent, finished, result, mhs);
        for (var i = 0; i < threads.length; i++) threads[i].start();

        while (threads[3].isAlive()) {
            try {
                Thread.sleep(5000);
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
                System.out.println("Thread " + i + " joined");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        File d = new File("books\\TestBase");
//        File d = new File("books\\English");
        File d = new File("books\\Spanish");
        TimeThis.currentlyTiming = true;

        TimeThis t = new TimeThis("Processamento para MinHash e BloomFilter", "e");
        BookDirectoryProcessor bookDirectoryProcessor = new BookDirectoryProcessor(d, new Mutable<Double>(1.0));
        t.end();
        compareAll(bookDirectoryProcessor.result);
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
                try {
                    System.out.println(String.format("%10.10s e %10.10s têm similaridade %5.5s", key1, key2, sim));
                } catch (Exception e) {
                }
            }
        }
        t.end();
    }
}
