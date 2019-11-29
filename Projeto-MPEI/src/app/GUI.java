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
        JButton searchTitle; //TODO check if its working AKA showing the actual book names
        JButton checkTitle;  //TODO check if tis working aka showing if the book is or is not a part of the inventory
        JButton requestBook; //TODO check if its working aka showing book request and seeing if it actually requested
        JButton returnBook;  //TODO check if its working AKA showing the book was returned and it being actually returned
        JButton similarTitle;
        JButton availability;
        JMenu file;
        JMenu tools;

        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        searchTitle = new JButton();
        searchTitle.addActionListener((e) -> {
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
        checkTitle.addActionListener((e) -> {
            checkTitle();
        });
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
        requestBook.addActionListener((e) -> {
            requestBook();
        });
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
        returnBook.addActionListener((e) -> {
            returnBook();
        });
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
        a.addActionListener((e) -> {
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
        a = new JMenuItem("Download data set");
        a.addActionListener((e) -> {
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
        Dimension windowSize = new Dimension(1000, 600);
        out.add(panel1);
        out.setSize(windowSize);
        out.setVisible(true);
        out.setJMenuBar(menus);
        out.setResizable(false);
        int x = (screenSize.width - out.getWidth()) / 2;
        int y = (screenSize.height - out.getHeight()) / 2;
        out.setLocation(x, y);
        out.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private static void returnBook() {
        JFrame p = new JFrame();
        p.setLayout(new BorderLayout());

        //header
        JLabel text = new JLabel();
        text.setText("Insert book name");
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setHorizontalAlignment(0);
        textPanel.add(text, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(400, 50));
        p.add(textPanel, BorderLayout.NORTH);

        //textfield
        JTextField title = new JTextField();
        JPanel insertPanel = new JPanel();
        insertPanel.setPreferredSize(new Dimension(310, 30));
        title.setPreferredSize(new Dimension(300, 25));
        insertPanel.add(title);
        p.add(insertPanel, BorderLayout.CENTER);

        //button
        JPanel searchPanel = new JPanel();
        JButton search = new JButton();
        search.setText("Return");
        search.addActionListener((e) -> {
            String book = title.getText();
            returnedBook(book);
        });
        search.setPreferredSize(new Dimension(150, 25));
        searchPanel.add(search);
        p.add(searchPanel, BorderLayout.SOUTH);


        p.setSize(400, 150);
        int x = (screenSize.width - p.getWidth()) / 2;
        int y = (screenSize.height - p.getHeight()) / 2;
        p.setLocation(x, y);
        p.setVisible(true);
        p.setResizable(false);
    }

    private static void returnedBook(String book) {
        String s = "";
        if (Interface.returnBook(book))
            s = "Book returned";
        else
            s = "Book not returned";
        JFrame window = new JFrame();
        window.setLayout(new GridLayout(1, 2));

        //title sort of
        JLabel text = new JLabel(s);
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setText(s);

        //button
        var okay = new JButton();
        okay.setText("OK");
        okay.addActionListener((e) -> {
            window.dispose();
        });

        //CLUSTER OF LEFT
        var left = new JPanel(new GridLayout(2, 1));
        var textP = new JPanel(new GridBagLayout());
        textP.add(text);
        left.add(textP);
        var okayP = new JPanel();
        okayP.add(okay, BorderLayout.CENTER);
        left.add(okayP);
        window.add(left);
        window.setSize(500, 300);
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
        window.setVisible(true);
        window.setResizable(false);
    }

    private static void requestBook() {
        JFrame p = new JFrame();
        p.setLayout(new BorderLayout());

        //header
        JLabel text = new JLabel();
        text.setText("Insert book name");
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setHorizontalAlignment(0);
        textPanel.add(text, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(400, 50));
        p.add(textPanel, BorderLayout.NORTH);

        //textfield
        JTextField title = new JTextField();
        JPanel insertPanel = new JPanel();
        insertPanel.setPreferredSize(new Dimension(310, 30));
        title.setPreferredSize(new Dimension(300, 25));
        insertPanel.add(title);
        p.add(insertPanel, BorderLayout.CENTER);

        //button
        JPanel searchPanel = new JPanel();
        JButton search = new JButton();
        search.setText("Request");
        search.addActionListener((e) -> {
            String book = title.getText();
            bookRequested(book);
        });
        search.setPreferredSize(new Dimension(150, 25));
        searchPanel.add(search);
        p.add(searchPanel, BorderLayout.SOUTH);


        p.setSize(400, 150);
        int x = (screenSize.width - p.getWidth()) / 2;
        int y = (screenSize.height - p.getHeight()) / 2;
        p.setLocation(x, y);
        p.setVisible(true);
        p.setResizable(false);

    }

    private static void bookRequested(String book) {
        String s = "";
        if (Interface.requestBook(book))
            s = "Book requested";
        else
            s = "Book not requested";
        JFrame window = new JFrame();
        window.setLayout(new GridLayout(1, 2));

        //title sort of
        JLabel text = new JLabel(s);
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setText(s);

        //button
        var okay = new JButton();
        okay.setText("OK");
        okay.addActionListener((e) -> {
            window.dispose();
        });

        //CLUSTER OF LEFT
        var left = new JPanel(new GridLayout(2, 1));
        var textP = new JPanel(new GridBagLayout());
        textP.add(text);
        left.add(textP);
        var okayP = new JPanel();
        okayP.add(okay, BorderLayout.CENTER);
        left.add(okayP);
        window.add(left);
        window.setSize(500, 300);
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
        window.setVisible(true);
        window.setResizable(false);
    }

    private static void checkTitle() {
        JFrame p = new JFrame();
        p.setLayout(new BorderLayout());

        //header
        JLabel text = new JLabel();
        text.setText("Insert title to check");
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setHorizontalAlignment(0);
        textPanel.add(text, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(400, 50));
        p.add(textPanel, BorderLayout.NORTH);

        //textfield
        JTextField title = new JTextField();
        JPanel insertPanel = new JPanel();
        insertPanel.setPreferredSize(new Dimension(310, 30));
        title.setPreferredSize(new Dimension(300, 25));
        insertPanel.add(title);
        p.add(insertPanel, BorderLayout.CENTER);

        //button
        JPanel searchPanel = new JPanel();
        JButton search = new JButton();
        search.setText("Check");
        search.addActionListener((e) -> {
            String book = title.getText();
            bookFound(book);
        });
        search.setPreferredSize(new Dimension(150, 25));
        searchPanel.add(search);
        p.add(searchPanel, BorderLayout.SOUTH);


        p.setSize(400, 150);
        int x = (screenSize.width - p.getWidth()) / 2;
        int y = (screenSize.height - p.getHeight()) / 2;
        p.setLocation(x, y);
        p.setVisible(true);
        p.setResizable(false);

    }

    private static void bookFound(String book) {
        //basically a copy of similar title
        String s = "";
        if (Interface.checkBook(book))
            s = "Book found";
        else
            s = "Book not found";
        JFrame window = new JFrame();
        window.setLayout(new GridLayout(1, 2));

        //title sort of
        JLabel text = new JLabel(s);
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setText(s);

        //button
        var okay = new JButton();
        okay.setText("OK");
        okay.addActionListener((e) -> {
            window.dispose();
        });

        //CLUSTER OF LEFT
        var left = new JPanel(new GridLayout(2, 1));
        var textP = new JPanel(new GridBagLayout());
        textP.add(text);
        left.add(textP);
        var okayP = new JPanel();
        okayP.add(okay, BorderLayout.CENTER);
        left.add(okayP);
        window.add(left);
        window.setSize(500, 300);
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
        window.setVisible(true);
        window.setResizable(false);

    }

    private static void searchTitle() {
        JFrame p = new JFrame();
        p.setLayout(new BorderLayout());

        //header
        JLabel text = new JLabel();
        text.setText("Insert title to search");
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setHorizontalAlignment(0);
        textPanel.add(text, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(400, 50));
        p.add(textPanel, BorderLayout.NORTH);

        //textfield
        JTextField title = new JTextField();
        JPanel insertPanel = new JPanel();
        insertPanel.setPreferredSize(new Dimension(310, 30));
        title.setPreferredSize(new Dimension(300, 25));
        insertPanel.add(title);
        p.add(insertPanel, BorderLayout.CENTER);

        //button
        JPanel searchPanel = new JPanel();
        JButton search = new JButton();
        search.setText("Search");
        search.addActionListener((e) -> {
            String book = title.getText();
            findSimilarTitle(book);
        });
        search.setPreferredSize(new Dimension(150, 25));
        searchPanel.add(search);
        p.add(searchPanel, BorderLayout.SOUTH);


        p.setSize(400, 150);
        int x = (screenSize.width - p.getWidth()) / 2;
        int y = (screenSize.height - p.getHeight()) / 2;
        p.setLocation(x, y);
        p.setVisible(true);
        p.setResizable(false);
    }

    private static void findSimilarTitle(String book) {
        JFrame window = new JFrame();
        window.setLayout(new GridLayout(1, 2));

        //title sort of
        JLabel text = new JLabel("Similar titles found");
        text.setFont(new Font("Arial", Font.BOLD, 25));

        //button
        var okay = new JButton();
        okay.setText("OK");
        okay.addActionListener((e) -> {
            window.dispose();
        });

        //CLUSTER OF LEFT
        var left = new JPanel(new GridLayout(2, 1));
        var textP = new JPanel(new GridBagLayout());
        textP.add(text);
        left.add(textP);
        var okayP = new JPanel();
        okayP.add(okay, BorderLayout.CENTER);
        left.add(okayP);
        window.add(left);

        //file thingy (CLUSTER OF RIGHT)
        JPanel rightPanel = new JPanel();
        JTextArea display = new JTextArea();
        String equalBooks = "";
        var equal = Interface.searchBook(book, 0.9); //<- parametrizar este valor??
        for (int i = 0; i < equal.size(); i++) {
            equalBooks = equalBooks + equal.get(i) + "\n";
        }
        display.setText(equalBooks);
        display.setPreferredSize(new Dimension(200, 250));
        display.setEditable(false);
        JScrollPane scroll = new JScrollPane(display);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(scroll);
        window.add(rightPanel);

        //JFrame settings
        window.setSize(500, 300);
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
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
        j.setSize(400, 100);
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
                } while (!progress.get().equals(1.0));
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

