/*
TODO 22/11/2019
 >fazer as janelas que faltam
 >linkar as janelas
 >actually pôr as funções

*/


package app;

import util.Mutable;

import javax.swing.*;
import java.awt.*;

import static app.Interface.parseDirectory;

class GUI {
    private static Mutable<Double> progress = new Mutable<>(0.0);
    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    static Dimension screenSize = toolkit.getScreenSize();

    public static void main(String[] args) {
        Dimension dim = new Dimension(300, 100); //para mudar o tamanho de cada botão
        JButton compare2Books;
        JButton similarContent;
        JButton parseDirectory;
        JButton searchTitle;
        JButton checkTitle;
        JButton requestBook;
        JButton returnBook;
        JButton similarTitle;
        JButton availability;
        JMenu file;
        JMenu tools;

        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        searchTitle = new JButton();
        searchTitle.addActionListener((e)->{
            /* TODO
                serch title
             */
            searchTitle();
        });
        searchTitle.setPreferredSize(dim);
        searchTitle.setText("Search title");
        JPanel button = new JPanel();
        button.add(searchTitle);
        button.setSize(dim);
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);


        checkTitle = new JButton();
        checkTitle.setText("Check title");
        checkTitle.setPreferredSize(dim);
        button = new JPanel();
        button.setSize(dim);
        button.add(checkTitle);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);


        requestBook = new JButton();
        requestBook.setText("Request book");
        requestBook.setPreferredSize(dim);
        button = new JPanel();
        button.setSize(dim);
        button.add(requestBook);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);

        returnBook = new JButton();
        returnBook.setText("Return book");
        returnBook.setPreferredSize(dim);
        button = new JPanel();
        button.setSize(dim);
        button.add(returnBook);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);


        similarTitle = new JButton();
        similarTitle.setText("Detect books with similar title");
        similarTitle.setPreferredSize(dim);
        button = new JPanel();
        button.setSize(dim);
        button.add(similarTitle);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);


        availability = new JButton();
        availability.setText("Check book availability");
        availability.setPreferredSize(dim);
        button = new JPanel();
        button.setSize(dim);
        button.add(availability);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);


        similarContent = new JButton();
        similarContent.setText("Detect books with similar content");
        similarContent.setPreferredSize(dim);
        button = new JPanel();
        button.setSize(dim);
        button.add(similarContent);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);


        compare2Books = new JButton();
        compare2Books.setText("Compare two books");
        compare2Books.setPreferredSize(dim);
        button = new JPanel();
        button.setSize(dim);
        button.add(compare2Books);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);


        parseDirectory = new JButton();
        parseDirectory.setText("Parse Directory");
        parseDirectory.setPreferredSize(dim);
        button = new JPanel();
        button.setSize(dim);
        button.add(parseDirectory);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(button, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        file = new JMenu("File");

        JMenuItem a = new JMenuItem("Open directory");
        /*
           TODO
            >implement Open directory
         */
        a.addActionListener((e)-> {
            System.out.println("Clicked open directory");
        });
        file.add(a);
        a = new JMenuItem("Save library");
        /*
         TODO
            >implement Save Library
        */
        a.addActionListener((e) -> {
            System.out.println("Clicked Save library");
        });
        file.add(a);
        a = new JMenuItem("Load library");
        /*
         TODO
            >implement Load Library
        */
        a.addActionListener((e) -> {
            System.out.println("Clicked Load Library");
        });
        file.add(a);
        a = new JMenuItem("Exit");
        a.addActionListener((e) -> {
            System.exit(0);
        });
        file.add(a);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(file, gbc);
        tools = new JMenu("Tools");
        a = new JMenuItem("Calculate Jaccard index");
        a.addActionListener((e) -> {
            /*
            TODO
                >Jaccard index
            */
            System.out.println("Clicked Jaccard index");
        });
        tools.add(a);
        a= new JMenuItem("Download data set");
        a.addActionListener((e)->{
            /* TODO
            *   >download data set (input será o número aproximado de livros)
            *   >com loading bar!!
            * */
            System.out.println("Clicked Download Data set");
        });
        tools.add(a);

        JMenuBar menus = new JMenuBar();
        menus.add(file);
        menus.add(tools);

        JFrame out = new JFrame("Library Management");
        Dimension windowSize = new Dimension(1000,600);
        out.add(panel1);
        out.setSize(windowSize);
        out.setVisible(true);
        out.setJMenuBar(menus);
        out.setResizable(false);
        int x = (screenSize.width - out.getWidth()) / 2;
        int y = (screenSize.height - out.getHeight()) / 2;
        out.setLocation(x,y);
        out.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private static void searchTitle() {
        JFrame p = new JFrame();
        p.setLayout(new FlowLayout());

        //header
        JLabel text = new JLabel();
        text.setText("Insert title to search");
        JPanel textPanel = new JPanel();
        textPanel.add(text);
        textPanel.setPreferredSize(new Dimension(500,100));
        textPanel.setLocation(p.getWidth(), p.getHeight()/3);// <- DOESN'T WORK
        p.add(textPanel);

        //textfield
        JTextField title = new JTextField();
        title.setPreferredSize(new Dimension(300,100));
        JPanel insertPanel= new JPanel();
        insertPanel.add(title);
        insertPanel.setSize(new Dimension(300,100));
        insertPanel.setLocation(p.getWidth(), p.getHeight()*2/3);
        p.add(insertPanel);

        JButton search = new JButton();
        search.setText("Search");
        search.addActionListener((e)->{
            String book=title.getText();
            findSimilarTitle(book);
        });
        search.setSize(new Dimension(300,100));
        p.add(search);
        search.addActionListener((e)-> {

        });


        p.setSize(500,300);
        int x = (screenSize.width - p.getWidth()) / 2;
        int y = (screenSize.height - p.getHeight()) / 2;
        p.setLocation(x,y);
        p.setVisible(true);
        p.setResizable(false);
    }

    private static void findSimilarTitle(String book) {
        JFrame window = new JFrame();
        window.setLayout(new FlowLayout());
        //title sort of
        JLabel text = new JLabel("Similar titles found");
        text.setSize(new Dimension(500,100));
        window.add(text);

        //file thingy


        //JFrame settings
        window.setSize(500,300);
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x,y);
        window.setVisible(true);
        window.setResizable(false);
    }

    public static void loadingExample() {
        JPanel p = new JPanel();
        final JButton b = new JButton("Botao");
        final JProgressBar pr = new JProgressBar();
        pr.setStringPainted(true);
        pr.setValue(0);
        pr.setSize(new Dimension(100, 23));
        p.add(pr);
        p.add(b);
        JFrame j = new JFrame();
        j.setSize(400,100);
        j.add(p);
        j.setVisible(true);
        final SwingWorker w = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                do {
                    try {
                        double p = progress.get();
                        pr.setValue((int) (p * 100));
                        j.repaint();
                        pr.setString((int) (p * 100) + "%");
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }while (progress.get() != 1.0);
                pr.setValue((int) (100));
                j.repaint();
                pr.setString((int) (100) + "%");
                return 0;
            }
        };
        b.addActionListener((e) -> {
            w.execute();
            parseDirectory(progress);
        });

    }
}
