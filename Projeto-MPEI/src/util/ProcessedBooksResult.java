package util;

import modules.BloomFilter;
import modules.MinHash;

public class ProcessedBooksResult {
    public MinHash minHashedTitle, minHashedContent;
    public static BloomFilter titlesBloomFilter = new BloomFilter(Enviroment.bloomFilterN, Enviroment.bloomFilterK);
}
