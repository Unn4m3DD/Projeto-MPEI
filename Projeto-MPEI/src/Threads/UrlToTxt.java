package Threads;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlToTxt extends Thread {
    private String targetURL;
    private int idx;
    private File dir;

    public UrlToTxt(int idx, File dir) {
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
            //ioe.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
