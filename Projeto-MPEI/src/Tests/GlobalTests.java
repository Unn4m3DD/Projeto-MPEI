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
            n = optimalNtest(params[0]);
        if (testsToPerform[1])
            optimalKFullTest();
        if (testsToPerform[2])
            falseNegativeTest();
        if (testsToPerform[3])
            falsePositiveFullTest();
        if (testsToPerform[4])
            testFalsePos();
        if (testsToPerform[5])
            testFalseNeg();
        if (testsToPerform[6])
            dispTest();
        if (testsToPerform[7])
            distTest();
        if (testsToPerform[8])
            minHashTest();
        if (testsToPerform[9])
            optimalNumberOfHashes(params[1]);

    }
}
