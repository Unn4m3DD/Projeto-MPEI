package util;

import util.TimeThis;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Book {
    private ArrayList<Character> title = new ArrayList<>(), content = new ArrayList<>();
    private String name;
    public Book(File f) {
        TimeThis t = new TimeThis("File to Book ");
        name = f.getName();
        boolean titleFetched = false;
        try (Scanner s = new Scanner(f)) {
            while (s.hasNextLine()) {
                String c_line = s.nextLine();
                if (!titleFetched && c_line.split(":")[0].equals("Title"))
                    for (var c : c_line.split(":")[1].trim().toCharArray()) {
                        titleFetched = true;
                        title.add(c);
                    }
                for (var c : c_line.toCharArray()) {
                    content.add(c);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        t.end();
    }

    public ArrayList<Character> getTitle() {
        return title;
    }

    public ArrayList<Character> getContent() {
        return content;
    }

    public String getName() {
        return name;
    }
}