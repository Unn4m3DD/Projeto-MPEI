package util;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeThis {
    long start;
    String s;
    String opc;
    public static boolean currentlyTiming, verbose, vverbose = false;
    static HashMap<String, ArrayList<Integer>> allTimes = new HashMap<>();

    public TimeThis(String s, String opc) {
        start = System.currentTimeMillis();
        this.s = s;
        this.opc = opc;
        if (!allTimes.containsKey(s))
            allTimes.put(s, new ArrayList<Integer>());
    }

    public TimeThis() {
        this("", "");
    }

    public TimeThis(String s) {
        this(s, "v");
    }

    public void end() {
        if (currentlyTiming) {
            allTimes.get(s).add((int) (System.currentTimeMillis() - start));
            if ((verbose && opc.equals("v")) || (vverbose && opc.equals("vv")) || opc.equals("e"))
                System.out.printf("%10.10s ms    %s\n", System.currentTimeMillis() - start, s);
        }
    }

    public static void printAllDelays() {
        for (String key : allTimes.keySet()) {
            int avg = 0;
            for (var single : allTimes.get(key))
                avg += single;
            if (allTimes.get(key).size() != 0)
                avg /= allTimes.get(key).size();
            else
                System.out.println();
            System.out.printf("%10.10s ms    %s\n", (avg), key);

        }
    }
}
