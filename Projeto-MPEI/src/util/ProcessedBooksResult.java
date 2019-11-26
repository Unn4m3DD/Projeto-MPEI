package util;

import modules.BloomFilter;
import modules.MinHash;

import java.io.Serializable;

public class ProcessedBooksResult implements Serializable {
    public MinHash minHashedTitle, minHashedContent;
    public String name;
    @Override
    public String toString() {
        return "name -> " + name;
    }
}
