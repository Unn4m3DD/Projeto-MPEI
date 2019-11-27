package util;

import modules.BloomFilter;
import modules.CountFilter;

import java.io.Serializable;
import java.util.HashMap;

public class AppState implements Serializable {
    private HashMap<String, ProcessedBooksResult> bookStockHashes;
    private BloomFilter availableBooks;
    private CountFilter stock;

    public AppState(HashMap<String, ProcessedBooksResult> bookStockHashes, BloomFilter availableBooks, CountFilter stock) {
        this.bookStockHashes = bookStockHashes;
        this.availableBooks = availableBooks;
        this.stock = stock;
    }

    public HashMap<String, ProcessedBooksResult> getBookStockHashes() {
        return bookStockHashes;
    }

    public BloomFilter getAvailableBooks() {
        return availableBooks;
    }

    public CountFilter getStock() {
        return stock;
    }
}
