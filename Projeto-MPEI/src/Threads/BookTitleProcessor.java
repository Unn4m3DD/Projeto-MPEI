package Threads;

import modules.BloomFilter;
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
    private BloomFilter availableBooks;
    public BookTitleProcessor(
            ConcurrentLinkedQueue<Book> toProcessTitle,
            ConcurrentLinkedQueue<Book> toProcessContent,
            Mutable<Boolean> finished,
            HashMap<String, ProcessedBooksResult> result,
            BloomFilter availableBooks
    ) {
        this.toProcessTitle = toProcessTitle;
        this.finished = finished;
        this.result = result;
        this.toProcessContent = toProcessContent;
        this.availableBooks = availableBooks;
    }

    public void run() {
        do {

            Book b = toProcessTitle.poll();
            while (b != null) {
                MinHash minHashedTitle = new MinHash(MinHash.shinglesHashCodeFromCharArr(b.getTitle(), titleShingleSize));

                StringBuilder sb = new StringBuilder();
                for (var c : b.getTitle()) {
                    sb.append(c);
                }
                availableBooks.addElement(sb.toString().trim());

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
