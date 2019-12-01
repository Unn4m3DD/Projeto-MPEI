package util;

import Threads.UrlToTxt;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;


public class BookDownloader extends Thread {
    File dir;
    Mutable<Double> progress;
    int numOfBooks;

    public BookDownloader(int numOfBooks, File dir, Mutable<Double> progress) {
        this.dir = dir;
        this.progress = progress;
        this.numOfBooks = numOfBooks;
    }

    @Override
    public void run() {
        downloadBooks(numOfBooks);
        System.gc();
        separateBooks(numOfBooks);
        System.gc();
        filterBooks(numOfBooks);
        System.gc();
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
            boolean endend = false;
            while (!endend) {
                progress.set(0.0);
                endend = true;
                for (var t : ts) {
                    if (!t.isAlive())
                        progress.set(progress.get() + 1.0 / ts.size());
                    else endend = false;
                }
                Thread.sleep(500);
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        progress.set(1.0);

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
                            fs.close();

                            if(!f.renameTo(new File(d.getAbsolutePath() + File.separator + idx + ".txt")))
                                System.out.println("erro");
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void filterBooks(int numOfBooks) {
        for (var f : dir.listFiles()) {
            if (!f.isDirectory() || f.listFiles().length == 0) {
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

