package Threads;

import modules.MinHash;
import modules.MinHashSeed;
import util.*;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.Enviroment.contentShingleSize;

public class BookContentProcessor extends Thread {
    ConcurrentLinkedQueue<Book> toProcessContent;
    HashMap<String, ProcessedBooksResult> result;
    Mutable<Boolean> finished;
    MinHashSeed minHashSeed;

    public BookContentProcessor(
            ConcurrentLinkedQueue<Book> toProcessContent,
            Mutable<Boolean> finished, HashMap<String, ProcessedBooksResult> result,
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
                TimeThis t = new TimeThis("1 Content MinHash", "v");
                MinHash minHashedContent = new MinHash(MinHash.shinglesFromCharArr(b.getContent(), contentShingleSize), minHashSeed);

                result.get(b.getName()).minHashedContent = minHashedContent;

                b = toProcessContent.poll();
                t.end();
            }
        } while (!finished.get());

    }
}
