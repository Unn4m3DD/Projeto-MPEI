package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.spliterator;

public class Book {
    private ArrayList<Character> title = new ArrayList<>(), content = new ArrayList<>();
    private String name;

//    public Book(File f) {
//        TimeThis t = new TimeThis("File to Book ");
//        name = f.getName();
//        boolean titleFetched = false;
//        try (Scanner s = new Scanner(f)) {
//            while (s.hasNextLine()) {
//                String c_line = s.nextLine();
//                if (!titleFetched && c_line.split(":")[0].equals("Title") && c_line.split(":").length > 1) {
//                    for (var c : c_line.split(":")[1].trim().toCharArray()) {
//                        title.add(c);
//                    }
//                    titleFetched = true;
//                }
//                for (var c : c_line.toCharArray()) {
//                    content.add(c);
//                }
//            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        t.end();
//    }

    public Book(File f) {
        TimeThis t = new TimeThis("File to Book ");
        name = f.getName();
        try {
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                String c_line = s.nextLine();
                if (c_line.split(":")[0].equals("Title") && c_line.split(":").length > 1) {
                    for (var c : c_line.split(":")[1].trim().toCharArray()) {
                        title.add(c);
                    }
                    break;
                }
            }
            Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.forName("UTF-8")).stream().
                    map(String::toCharArray).forEach(e -> {
                for (var elem : e) content.add(elem);
            });
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