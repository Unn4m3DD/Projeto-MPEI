package Threads;

import modules.MinHash;
import modules.MinHashSeed;
import util.Book;
import util.Enviroment;
import util.MutableBoolean;
import util.ProcessedBooksResult;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookContentProcessor extends Thread {
    ConcurrentLinkedQueue<Book> toProcessContent;
    HashMap<String, ProcessedBooksResult> result;
    MutableBoolean finished;
    MinHashSeed minHashSeed;

    public BookContentProcessor(
            ConcurrentLinkedQueue<Book> toProcessContent,
            MutableBoolean finished, HashMap<String, ProcessedBooksResult> result,
            MinHashSeed minHashSeed
    ) {
        this.toProcessContent = toProcessContent;
        this.finished = finished;
        this.result = result;
        this.minHashSeed = minHashSeed;
    }

    public void run() {
        do {
            Book b = toProcessContent.poll();
            while (b != null) {
                MinHash minHashedContent = new MinHash(MinHash.shinglesFromFile(b.getContent(), Enviroment.contentShingleSize), minHashSeed);

                if (!result.containsKey(b.getName())) {
                    ProcessedBooksResult innerResult = new ProcessedBooksResult();
                    innerResult.minHashedContent = minHashedContent;
                    result.put(b.getName(), innerResult);
                } else {
                    result.get(b.getName()).minHashedContent = minHashedContent;
                }
                b = toProcessContent.poll();
            }
        }while (!finished.getB());

    }
}
