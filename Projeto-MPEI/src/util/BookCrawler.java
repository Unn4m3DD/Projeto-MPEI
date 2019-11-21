package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


class BookCrawler {

    public static void main(String[] args) throws InterruptedException {
//        downloadBooks();

//        separateBooks();

        filterBooks();
    }


    private static void downloadBooks() throws InterruptedException {

        long start = System.currentTimeMillis();
        Thread lastThread = new Thread();
        for (var i = 0; i < 50000; i++) {
            UrlToTxt t = new UrlToTxt(i);
            t.start();
            lastThread = t;
        }
        lastThread.join();
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("util.Book Download || util.Book Verification " + timeElapsed + "ms");
    }

    private static void separateBooks() {
        long start = System.currentTimeMillis();
        for (var idx = 0; idx < 50000; idx++) {
            try {
                File f = new File("books/" + idx + ".txt");
                if (f.exists()) {
                    Scanner fs = new Scanner(f);
                    while (fs.hasNextLine()) {
                        String[] split = fs.nextLine().split(":");

                        if (split.length > 0 && split[0].trim().equals("Language")) {
                            File d = new File("books/" + split[1].trim());
                            if (!d.exists())
                                d.mkdir();
                            fs.close();
                            f.renameTo(new File("D:\\dev\\Projeto-MPEI\\books\\" + split[1].trim() + "\\" + idx + ".txt"));
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("util.Book Separation " + timeElapsed + "ms");

    }


    private static void filterBooks() {
        long start = System.currentTimeMillis();
        File d = new File("books");
        for (var f : d.listFiles()) {
            if (!f.isDirectory() || f.listFiles().length < 50) {
                deleteDir(f);
            }
        }
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("util.Book Filtering " + timeElapsed + "ms");
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


class UrlToTxt extends Thread {
    private String targetURL;
    private int idx;

    UrlToTxt(int idx) {
        targetURL = "http://www.gutenberg.org/cache/epub/" + idx + "/pg" + idx + ".txt";
        this.idx = idx;
    }

    public void run() {
        HttpURLConnection connection = null;
        try {
            File f = new File("books/" + idx + ".txt");
            if (f.exists()) {
                return;
            }
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setConnectTimeout(connection.getConnectTimeout() * 10);
            connection.setReadTimeout(connection.getReadTimeout() * 10);
            //Send request
            new DataOutputStream(connection.getOutputStream());


            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            String line;
            FileWriter fw = new FileWriter(f);
            while ((line = rd.readLine()) != null) {
                fw.write(line);
                fw.write("\n");
            }
            fw.close();
            rd.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
