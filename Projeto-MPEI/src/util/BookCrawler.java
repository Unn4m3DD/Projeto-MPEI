package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;


public class BookCrawler {
    File dir;

    public BookCrawler(int numOfBooks, File dir) {
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


class UrlToTxt extends Thread {
    private String targetURL;
    private int idx;
    private File dir;

    UrlToTxt(int idx, File dir) {
        targetURL = "http://www.gutenberg.org/cache/epub/" + idx + "/pg" + idx + ".txt";
        this.idx = idx;
        this.dir = dir;
    }

    public void run() {
        HttpURLConnection connection = null;
        try {
            File f = new File(dir.getAbsolutePath() + File.separator + idx + ".txt");
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
            InputStream is;
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                String line;
                FileWriter fw = new FileWriter(f);
                while ((line = rd.readLine()) != null) {
                    fw.write(line);
                    fw.write("\n");
                }
                fw.close();
                rd.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
