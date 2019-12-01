package Threads;

import modules.MinHash;
import util.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.Environment.*;

public class BookContentProcessor extends Thread {
    private ConcurrentLinkedQueue<Book> toProcessContent;
    private HashMap<String, ProcessedBooksResult> result;
    private Mutable<Boolean> finished;

    public BookContentProcessor(
            ConcurrentLinkedQueue<Book> toProcessContent,
            Mutable<Boolean> finished, HashMap<String, ProcessedBooksResult> result
    ) {
        this.toProcessContent = toProcessContent;
        this.finished = finished;
        this.result = result;
    }

    public void run() {
        do {
            Book b = toProcessContent.poll();
            while (b != null) {
                TimeThis t = new TimeThis("1 Content MinHash", "v");
                result.get(b.getName()).minHashedContent = new MinHash(MinHash.shinglesHashCodeFromCharArr(b.getContent(), contentShingleSize));
                b = toProcessContent.poll();
                t.end();
            }
        } while (!finished.get());

    }
}
