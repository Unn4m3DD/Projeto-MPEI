package app;

import util.BookCrawler;
import util.BookDirectoryProcessor;
import util.Mutable;
import util.ProcessedBooksResult;

import java.io.*;
import java.util.HashMap;

class Interface {
    private static File currentDir = new File("books" + File.separator + "TestBase");

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
            assert true;
        save(new File("savetest.ser"));
        currentData.size();
        load(new File("savetest.ser"));
        System.out.println(currentData.size());
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
        in.close();
        file.close();
    }


}
