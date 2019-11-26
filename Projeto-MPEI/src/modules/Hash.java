package modules;


import static util.Environment.numberOfHashesForMinHash;

public class Hash {
    public static HashSeed hashSeed = new HashSeed(numberOfHashesForMinHash);

    public static int hash(int item, int i){
       return hashSeed.a[i] * item + hashSeed.b[i];
    }
}
