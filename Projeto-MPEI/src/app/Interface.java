package app;

import modules.MinHash;
import util.BookCrawler;
import util.BookDirectoryProcessor;
import util.Mutable;
import util.ProcessedBooksResult;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static util.Environment.titleShingleSize;
import static util.ProcessedBooksResult.titlesBloomFilter;

class Interface {
    private static File currentDir = new File("books" + File.separator + "Spanish");

    public static File setCurrentDirectory() {
        return currentDir;
    }

    //This function may not download numOfBooks, this argument is just an indication
    public static void downloadTestData(int numOfBooks) {
        System.err.println("não te esqueças de indicar que o argumento é meramente indicativo");
        new BookCrawler(numOfBooks, currentDir);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Mutable<Double> prog = new Mutable<>(0.0);
        parseDirectory(prog);
        while (!prog.get().equals(1.0))
           try{
               Thread.sleep(1000);
           } catch (InterruptedException ie){
               ie.printStackTrace();
           }
        save(new File("savetest.ser"));
        load(new File("savetest.ser"));
        System.out.println(searchBook("Merodeadores ", .1));
        System.out.println(currentData);
        System.out.println(checkBook("Los majos de Cádiz"));
        System.out.println(checkBook("Los pescadores de Trépang"));
    }

    public static boolean checkBook(String name) {
        return titlesBloomFilter.isElement(name);
    }

    public static List<ProcessedBooksResult> searchBook(String name, double thr) {
        ArrayList<ProcessedBooksResult> result = new ArrayList<>();
        ArrayList<Character> list = new ArrayList<>();
        for (char c : name.toCharArray()) {
            list.add(c);
        }

        MinHash nameMinHash =
                new MinHash(
                        MinHash.shinglesFromCharArr(list, titleShingleSize)
                );
        for (var item : currentData.values()) {
            if (item.minHashedTitle.calcSimTo(nameMinHash) > thr) {
                result.add(item);
            }
        }
        return result;
    }

    private static HashMap<String, ProcessedBooksResult> currentData = new HashMap<>();

    public static void parseDirectory(Mutable<Double> progress) {
        BookDirectoryProcessor processor = new BookDirectoryProcessor(currentDir, progress);
        processor.start();
        currentData = processor.result;
    }

    public static void save(File destination) throws IOException {
        FileOutputStream file = new FileOutputStream(destination);
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(currentData);
        out.close();
        file.close();
    }

    public static void load(File source) throws IOException, ClassNotFoundException {
        FileInputStream file = new FileInputStream(source);
        ObjectInputStream in = new ObjectInputStream(file);
        currentData = (HashMap<String, ProcessedBooksResult>) in.readObject();
        for(var item: currentData.values()){
            titlesBloomFilter = item.sertitlesBloomFilter;
            break;
        }
        in.close();
        file.close();
    }


}
