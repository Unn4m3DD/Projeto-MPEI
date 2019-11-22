package modules;

public class CountFilter {
    private int n, k;
    private int[] filter;

    public CountFilter(int n, int k) {
        this.n = n;
        this.k = k;
        this.filter = new int[n];
    }

    //para requisitar usar-se-há este método
    public void addElement(String elem, BloomFilter bloom) {
        if (bloom.isElement(elem) && ! isElement(elem)){  //verifica se o livro existe na biblioteca e não está requisitado
            var cond = true;
            for (var i = 0; i < k; i++) {
                //a hash usada vai variando segundo a iteração, assim usamos 2 métodos de "hashar" diferentes
                if (cond)
                    filter[Math.abs((elem + i).hashCode() % n)] ++;
                else
                    filter[string2hash(elem, n, i)] ++;
                cond = !cond;
            }
        }

    }

    //para verificar se está requisitado usar-se-há este método
    public boolean isElement(String elem) {
        var cond = true;
        for (var i = 0; i < k; i++) {
            if (cond && filter[Math.abs((elem + i).hashCode() % n)] == 0) {
                return false;
            } else if (!cond && filter[string2hash(elem, n, i)] == 0) {
                return false;
            }
            cond = !cond;
        }
        return true;
    }


    public boolean returnBook(String elem, BloomFilter bloom){  //returns true if it was returned, returns false if wasn't
        if (! isElement(elem)) {
            System.out.println("Book was not taken from the library");
            return false;
        }
        if (! bloom.isElement(elem)) {
            System.out.println("Book doesn't exist in the library");
            return false;
        }
        var cond = true;
        for (var i = 0; i < k; i++) {
            //código do método addElement() mas em vez de incrementar decrementa
            if (cond)
                filter[Math.abs((elem + i).hashCode() % n)] --;
            else
                filter[string2hash(elem, n, i)] --;
            cond = !cond;
        }
        return true;

    }

    private int string2hash(String s, int mod, int seed) {
        long result = (long) Math.pow(seed, seed * 3);
        char[] ca = s.toCharArray();
        for (var c : ca) {
            result = result * 31 + c;
        }
        result = Math.abs(result);
        return (int) (result % mod);
    }

    public int getN() {
        return n;
    }

    public int getK() {
        return k;
    }

    public int[] getFilter() {
        return filter;
    }
}
