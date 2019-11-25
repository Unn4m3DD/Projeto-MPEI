package util;

import modules.BloomFilter;
import modules.HashSeed;

public abstract class Environment {
    public static int bloomFilterN = 1000;
    public static int bloomFilterK = BloomFilter.optimalK(1000, 5000);
    public static int titleShingleSize = 4;
    public static int contentShingleSize = 8;
    private static int numberOfHashesForMinHash = 150;
    public static HashSeed hashSeed = new HashSeed(numberOfHashesForMinHash);

}
