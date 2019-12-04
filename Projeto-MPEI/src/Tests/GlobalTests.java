package Tests;

import modules.BloomFilter;
import util.ConsoleGui;

import javax.swing.*;
import java.io.PrintStream;

import static Tests.BloomFilterTest.*;
import static Tests.CountFilterTest.*;
import static Tests.HashTest.*;
import static Tests.MinHashTest.*;

public class GlobalTests extends Thread {
    JTextArea jta;
    int numberOfLines;
    boolean[] testsToPerform;
    double[] params;
    public GlobalTests(JTextArea jta, int numberOfLines, boolean[] testsToPerform, double[] params) {
        this.jta = jta;
        this.numberOfLines = numberOfLines;
        this.testsToPerform = testsToPerform;
        this.params = params;
    }

    @Override
    public void run() {
        PrintStream con = new PrintStream(new ConsoleGui(jta, numberOfLines));
        System.setOut(con);
        System.setErr(con);
        if (testsToPerform.length != 10)
            System.out.print("An error occurred and all tests will be perfomed");

        BloomFilter b = BloomFilter.fromFile("mises.txt", 10);

        if (testsToPerform[0])
            optimalKFullTest();
        else if (testsToPerform[1])
            n = optimalNtest(params[0]);
        else if (testsToPerform[2])
            falseNegativeTest(b);
        else if (testsToPerform[3])
            falsePositiveFullTest(b);
        else if (testsToPerform[4])
            testFalsePos();
        else if (testsToPerform[5])
            testFalseNeg();
        else if (testsToPerform[6])
            dispTest();
        else if (testsToPerform[7])
            distTest();
        else if (testsToPerform[8])
            optimalSimilarity();
        else if (testsToPerform[9])
            optimalNumberOfHashes(params[1]);

    }
}
