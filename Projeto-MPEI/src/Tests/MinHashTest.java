package Tests;

import modules.MinHash;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class MinHashTest {
    public static int shingleSize = 4;
    public static int fileSize = 300000;
    public static void main(String[] args) throws FileNotFoundException {
        testSimilarity();
    }

    private static void testSimilarity() throws FileNotFoundException {
        File [] files = createFiles();

        double[] similarity = new double[files.length-1];
        MinHash originalfile = getMinHash(files[0]);
        for(int i=0; i<similarity.length; i++){
            similarity[i] =  originalfile.calcSimTo(getMinHash(files[i+1])) * 100 ;
        }
        for(int i=0; i< similarity.length; i++){
            System.out.println("Similarity between OG file and "+((i+1)*10)+": "+similarity[i]);
        }
        //TODO do the same but with jaccardindex
    }

    private static MinHash getMinHash(File file) throws FileNotFoundException {
        Scanner fileReader = new Scanner(file);
        String sentence = "";
        while(fileReader.hasNext()){
            sentence += fileReader.next();
        }
        var charArray = sentence.toCharArray();
        ArrayList <Character> charList = new ArrayList<>();
        for(int i=0; i<charArray.length; i++){
            charList.add(charArray[i]);
        }
        fileReader.close();
        var shingles = MinHash.shinglesFromCharArr(charList,shingleSize);
        return new MinHash(shingles);
    }

    private static File [] createFiles() throws FileNotFoundException {
        File [] fileArr = new File[11];
        fileArr[0] = new File ("Projeto-MPEI\\src\\Tests\\test\\test.txt");
        PrintWriter write = new PrintWriter(fileArr[0]);
        for(int i=0; i< fileSize; i++){
            write.print("a");
        }
        write.close();

        for(int i=1; i<=10; i++){
            fileArr[i] = new File("Projeto-MPEI\\src\\Tests\\test\\test"+i*10+".txt");
            PrintWriter writeFiles = new PrintWriter(fileArr[i]);
            for(int x=0; x< fileSize; x++){
                if(Math.random()*100 < i*10)
                    writeFiles.print("a");
                else
                    writeFiles.print("b");
            }
            writeFiles.close();
        }
        //file index * 10 equals the degree of similarity with the first element of the file array
        return fileArr;
    }
}

