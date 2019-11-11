public class BloomFilter {
    private int n, k;
    private boolean[] filter;

    public BloomFilter(int n, int k) {
        this.n = n;
        this.k = k;
        this.filter = new boolean[n];
    }
    void addElement(String elem){
        for(var i = 0; i < k; i++){
            filter[(elem + i).hashCode() % n] = true;
        }
    }
    boolean isElement(String elem){
        for(var i = 0; i < k; i++){
            if(!filter[(elem + i).hashCode() % n]){
                return false;
            }
        }
        return true;
    }
}
