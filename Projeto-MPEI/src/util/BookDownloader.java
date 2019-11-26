package util;

import Threads.UrlToTxt;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;


public class BookDownloader {
    File dir;

    public BookDownloader(int numOfBooks, File dir) {
        this.dir = dir;
        downloadBooks(numOfBooks);
        separateBooks(numOfBooks);
        filterBooks(numOfBooks);
        deleteLeftFiles();
    }

    private void deleteLeftFiles() {
        for (File f : dir.listFiles()) {
            if (f.isFile())
                while (!f.delete()) {
                }
        }
    }


    private void downloadBooks(int numOfBooks) {
        LinkedList<Thread> ts = new LinkedList<>();
        for (var i = 0; i < numOfBooks; i++) {
            UrlToTxt t = new UrlToTxt(i, dir);
            t.start();
            ts.add(t);
        }
        try {
            for (var t : ts) {
                t.join();
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    private void separateBooks(int numOfBooks) {
        for (var idx = 0; idx < numOfBooks; idx++) {
            try {
                File f = new File(dir.getAbsolutePath() + File.separator + idx + ".txt");
                if (f.exists()) {
                    Scanner fs = new Scanner(f);
                    while (fs.hasNextLine()) {
                        String[] split = fs.nextLine().split(":");

                        if (split.length > 0 && split[0].trim().equals("Language")) {
                            File d = new File(dir.getAbsolutePath() + File.separator + split[1].trim());
                            if (!d.exists())
                                d.mkdir();
                            f.renameTo(new File(dir.getAbsolutePath() + File.separator + split[1].trim() + File.separator + idx + ".txt"));
                            break;
                        }
                    }
                    fs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void filterBooks(int numOfBooks) {
        for (var f : dir.listFiles()) {
            if (!f.isDirectory() || f.listFiles().length < numOfBooks / 100) {
                deleteDir(f);
            }
        }
    }

    private static void deleteDir(File d) {
        if (d.isDirectory()) {
            for (var f : d.listFiles()) {
                deleteDir(f);
            }
        }
        d.delete();
    }

}

