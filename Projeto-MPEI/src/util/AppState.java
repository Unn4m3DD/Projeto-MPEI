package util;

import modules.BloomFilter;
import modules.CountFilter;

import java.io.Serializable;
import java.util.HashMap;

public class AppState implements Serializable {
    private HashMap<String, ProcessedBooksResult> bookStockHashes;
    private BloomFilter availableBooks;
    private CountFilter requestedBooks;
    private HashMap<Pair<String, String>, Pair<Double, Double>> similarity;

    public AppState(
            HashMap<String, ProcessedBooksResult> bookStockHashes,
            BloomFilter availableBooks,
            CountFilter requestedBooks,
            HashMap<Pair<String, String>, Pair<Double, Double>> similarity
    ) {
        this.bookStockHashes = bookStockHashes;
        this.availableBooks = availableBooks;
        this.requestedBooks = requestedBooks;
        this.similarity = similarity;
    }

    public HashMap<Pair<String, String>, Pair<Double, Double>> getSimilarity() {
        return similarity;
    }

    public HashMap<String, ProcessedBooksResult> getBookStockHashes() {
        return bookStockHashes;
    }

    public BloomFilter getAvailableBooks() {
        return availableBooks;
    }

    public CountFilter getRequestedBooks() {
        return requestedBooks;
    }
}
