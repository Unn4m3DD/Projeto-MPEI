package util;

import modules.BloomFilter;
import modules.MinHash;

import java.io.Serializable;

public class ProcessedBooksResult implements Serializable {
    public MinHash minHashedTitle, minHashedContent;
    public static BloomFilter titlesBloomFilter = new BloomFilter(Environment.bloomFilterN, Environment.bloomFilterK);
    public String name;
    public BloomFilter sertitlesBloomFilter;
    public ProcessedBooksResult(){
        sertitlesBloomFilter = titlesBloomFilter;
    }
    @Override
    public String toString() {
        return "name -> " + name;
    }
}
