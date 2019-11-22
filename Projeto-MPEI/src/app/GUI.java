package app;

import util.Mutable;

import javax.swing.*;
import java.awt.*;

import static app.Interface.parseDirectory;

class GUI {
    private static Mutable<Double> progress = new Mutable<>(0.0);


    public static void main(String[] args) {
        JPanel p = new JPanel();
        final JButton b = new JButton("Botao");
        final JProgressBar pr = new JProgressBar();
        pr.setStringPainted(true);
        pr.setValue(0);
        pr.setSize(new Dimension(100, 23));
        p.add(pr);
        p.add(b);
        JFrame j = new JFrame();
        j.add(p);
        j.setVisible(true);
        final SwingWorker w = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                while (progress.get() < 0.98) {
                    try {
                        double p = progress.get();
                        pr.setValue((int) (p * 100));
                        j.repaint();
                        pr.setString((int) (p * 100) + "%");
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                return 0;
            }
        };
        b.addActionListener((e) -> {
            w.execute();
            parseDirectory(progress);
        });
    }
}
