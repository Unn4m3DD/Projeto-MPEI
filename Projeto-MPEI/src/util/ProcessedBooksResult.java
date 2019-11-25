package util;

import modules.BloomFilter;
import modules.MinHash;

import java.io.Serializable;

public class ProcessedBooksResult implements Serializable {
    public MinHash minHashedTitle, minHashedContent;
    public static BloomFilter titlesBloomFilter = new BloomFilter(Environment.bloomFilterN, Environment.bloomFilterK);
    // TODO: 21/11/2019 adicionar nome da file e titulo do livro 
}
