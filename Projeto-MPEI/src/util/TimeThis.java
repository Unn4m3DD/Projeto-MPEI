package util;

public class TimeThis {
    long start;
    String s;
    String opc;
    public static boolean currentlyTiming, verbose, vverbose = false;
    public TimeThis(String s, String opc) {
        start = System.currentTimeMillis();
        this.s = s;
        this.opc = opc;
    }
    public TimeThis() {
        this("", "");
    }
    public TimeThis(String s) {
        this(s, "v");
    }

    public void end() {
        if(currentlyTiming){
            if((verbose && opc.equals("v")) || (vverbose && opc.equals("vv")) || opc.equals("e"))
                System.out.printf("%10.10s ms    %s\n", System.currentTimeMillis() - start, s);
        }
    }
}
