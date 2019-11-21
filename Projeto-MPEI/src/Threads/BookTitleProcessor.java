package Threads;

import modules.MinHash;
import modules.MinHashSeed;
import util.Book;
import util.Mutable;
import util.ProcessedBooksResult;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.Enviroment.*;

public class BookTitleProcessor extends Thread {
    ConcurrentLinkedQueue<Book> toProcessTitle, toProcessContent;
    HashMap<String, ProcessedBooksResult> result;
    Mutable<Boolean> finished;

    public BookTitleProcessor(
            ConcurrentLinkedQueue<Book> toProcessTitle,
            ConcurrentLinkedQueue<Book> toProcessContent,
            Mutable<Boolean> finished,
            HashMap<String, ProcessedBooksResult> result
    ) {
        this.toProcessTitle = toProcessTitle;
        this.finished = finished;
        this.result = result;
        this.toProcessContent = toProcessContent;
    }

    public void run() {
        do {

            Book b = toProcessTitle.poll();
            while (b != null) {
                toProcessContent.add(b);
                MinHash minHashedTitle = new MinHash(MinHash.shinglesFromCharArr(b.getTitle(), titleShingleSize), minHashSeed);

                ProcessedBooksResult.titlesBloomFilter.addElement(b.getTitle().toString());

                if (!result.containsKey(b.getName())) {
                    ProcessedBooksResult innerResult = new ProcessedBooksResult();
                    innerResult.minHashedTitle = minHashedTitle;
                    result.put(b.getName(), innerResult);
                } else {
                    result.get(b.getName()).minHashedTitle = minHashedTitle;
                }
                b = toProcessTitle.poll();
            }
            try {
                sleep(200);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        } while (!finished.get());

    }
}
