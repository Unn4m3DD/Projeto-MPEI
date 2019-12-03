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

    public GlobalTests(JTextArea jta, int numberOfLines, boolean[] testsToPerform) {
        this.jta = jta;
        this.numberOfLines = numberOfLines;
        this.testsToPerform = testsToPerform;
    }

    @Override
    public void run() {
        PrintStream con = new PrintStream(new ConsoleGui(jta, numberOfLines));
        if (testsToPerform.length != 10)
            System.out.print("An error occurred and all tests will be perfomed");

        BloomFilter b = BloomFilter.fromFile("mises.txt", n);

        if (testsToPerform[0])
            optimalKFullTest();
        else if (testsToPerform[1])
            n = optimalNtest();
        else if (testsToPerform[2])
            falseNegativeTest(b);
        else if (testsToPerform[3])
            falsePositiveFullTest(b);
        else if (testsToPerform[4])
            testFalsePos();
        else if (testsToPerform[5])
            testFalseNeg();
        else if (testsToPerform[6])
            hashTest();
        else if (testsToPerform[7])
            dispTest();
        else if (testsToPerform[8])
            optimalNumberOfHashes();
        else if (testsToPerform[9])
            optimalSimilarity();


        System.setOut(con);
        System.setErr(con);
        HashTest.main(new String[0]);
        BloomFilterTest.main(new String[0]);
        CountFilterTest.main(new String[0]);
        MinHashTest.main(new String[0]);
    }
}
