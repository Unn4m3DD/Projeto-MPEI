package Threads;

import modules.MinHash;
import util.Book;
import util.Mutable;
import util.ProcessedBooksResult;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static util.Environment.titleShingleSize;

public class BookTitleProcessor extends Thread {
    private ConcurrentLinkedQueue<Book> toProcessTitle;
    private ConcurrentLinkedQueue<Book> toProcessContent;
    private HashMap<String, ProcessedBooksResult> result;
    private Mutable<Boolean> finished;

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
                MinHash minHashedTitle = new MinHash(MinHash.shinglesFromCharArr(b.getTitle(), titleShingleSize));

                StringBuilder sb = new StringBuilder();
                for (var c : b.getTitle()) {
                    sb.append(c);
                }
                ProcessedBooksResult.titlesBloomFilter.addElement(sb.toString().trim());

//                if (!result.containsKey(b.getName())) {
                ProcessedBooksResult innerResult = new ProcessedBooksResult();
                innerResult.minHashedTitle = minHashedTitle;

                innerResult.name = sb.toString().trim();
                result.put(b.getName(), innerResult);
//                } else {
//                    result.get(b.getName()).minHashedTitle = minHashedTitle;
//                }
                toProcessContent.add(b);
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
