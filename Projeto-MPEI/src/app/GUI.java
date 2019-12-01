/*
TODO 22/11/2019
 >fazer as janelas que faltam
 >linkar as janelas
 >actually pôr as funções

*/


package app;

import util.Mutable;
import util.ProcessedBooksResult;
import util.SimContainer;
import util.TimeThis;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static app.Interface.*;

/* D:\dev\Projeto-MPEI\books\Spanish */
class GUI extends JFrame implements ActionListener {
    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    static Dimension screenSize = toolkit.getScreenSize();
    private static Mutable<Double> progress = new Mutable<>(0.0);
    HashMap<String, JButton> buttons = new HashMap<>();
    HashMap<String, JPanel> panels = new HashMap<>();
    HashMap<String, JMenuItem> menuItems = new HashMap<>();
    String[] buttonsKeys = new String[]{
            "Compare 2 Books", "All Similar Content", //"Parse Directory",
            "Search Title", "Check Title", "Request Book", "Return Book",
            "All Similar Title", "Check book Availability"};
    String[] fileMenuItemKeys = new String[]{"Parse Directory", "Save Library", "Load Library", "Exit"};
    String[] toolsMenuItemKeys = new String[]{"Calculate Jaccard Index", "Download Data Set"};

    //TODO check if its working AKA showing the actual book names
    //TODO check if tis working aka showing if the book is or is not a part of the inventory
    //TODO check if its working aka showing book request and seeing if it actually requested
    //TODO check if its working AKA showing the book was returned and it being actually returned

    private void updateButtonClickability() {
        for (var button : buttons.values()) {
            button.setEnabled(bookStockHashes.keySet().size() != 0);
        }
    }

    private void initializeButtonsAndPanels() {
        Dimension buttonSize = new Dimension(300, 100);
        for (var s : buttonsKeys) {
            buttons.put(s, new JButton(s));
            buttons.get(s).addActionListener(this);
            buttons.get(s).setPreferredSize(buttonSize);
            buttons.get(s).setText(s);
            panels.put(s, new JPanel());
            panels.get(s).add(buttons.get(s));
            add(panels.get(s));
        }
        JMenuBar menus = new JMenuBar();
        JMenu file = new JMenu("File");
        for (var s : fileMenuItemKeys) {
            menuItems.put(s, new JMenuItem(s));
            menuItems.get(s).addActionListener(this);
            file.add(menuItems.get(s));
        }
        menus.add(file);
        JMenu tools = new JMenu("Tools");
        for (var s : toolsMenuItemKeys) {
            menuItems.put(s, new JMenuItem(s));
            menuItems.get(s).addActionListener(this);
            tools.add(menuItems.get(s));
        }
        menus.add(tools);
        setJMenuBar(menus);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(buttons.get("Search Title")))
            searchTitle();
        if (e.getSource().equals(buttons.get("Check Title")))
            checkTitle();
        if (e.getSource().equals(buttons.get("Request book")))
            requestBook();
        if (e.getSource().equals(buttons.get("Return book")))
            returnBook();
        if (e.getSource().equals(buttons.get("All Similar Title")))
            allSimilarTitle();
        if (e.getSource().equals(buttons.get("Check book Availability")))
            assert true;
        if (e.getSource().equals(buttons.get("All Similar Content")))
            allSimilarContent();
        if (e.getSource().equals(buttons.get("Compare 2 Books")))
            assert true;
        if (e.getSource().equals(menuItems.get("Parse Directory")))
            parsePopupDirectory();
        if (e.getSource().equals(menuItems.get("Save Library")))
            saveLibrary();
        if (e.getSource().equals(menuItems.get("Load Library")))
            loadLibrary();
    }


    private GUI(String windowHeader) throws IOException, ClassNotFoundException {
        super(windowHeader);
        load(new File("./save.ser"));
        initializeButtonsAndPanels();
        setLayout(new GridLayout(3, 3));
        Dimension windowSize = new Dimension(1000, 600);
        setSize(windowSize);
        setVisible(true);
        setResizable(false);
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        updateButtonClickability();
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        (new GUI("Biblioteca")).setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                TimeThis.printAllDelays();
            }
        });
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
        JFrame checkTitlePopup = new JFrame("Check Title");
        checkTitlePopup.setLayout(new BorderLayout());
        //header
        JLabel text = new JLabel();
        text.setText("Insert title to check");
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setHorizontalAlignment(0);
        textPanel.add(text, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(400, 50));
        checkTitlePopup.add(textPanel, BorderLayout.NORTH);
        //textfield
        JTextField title = new JTextField();
        JPanel insertPanel = new JPanel();
        insertPanel.setPreferredSize(new Dimension(310, 30));
        title.setPreferredSize(new Dimension(300, 25));
        insertPanel.add(title);
        checkTitlePopup.add(insertPanel, BorderLayout.CENTER);
        //button
        JPanel searchPanel = new JPanel();
        JButton search = new JButton();
        search.setText("Check");
        search.addActionListener((e) -> {
            //basically a copy of similar title
            String book = title.getText();
            String s;
            if (checkBook(book))
                s = "Book found";
            else
                s = "Book not found";
            JFrame window = new JFrame("Check Title");
            window.setLayout(new GridLayout(1, 2));
            //title sort of
            JLabel innerText = new JLabel(s);
            innerText.setFont(new Font("Arial", Font.BOLD, 25));
            innerText.setText(s);
            //button
            var okay = new JButton();
            okay.setText("OK");
            okay.addActionListener((event) -> {
                window.dispose();
            });
            //CLUSTER OF LEFT
            var left = new JPanel(new GridLayout(2, 1));
            var textP = new JPanel(new GridBagLayout());
            textP.add(innerText);
            left.add(textP);
            var okayP = new JPanel();
            okayP.add(okay, BorderLayout.CENTER);
            left.add(okayP);
            window.add(left);
            window.setSize(400, 150);
            int x = (screenSize.width - window.getWidth()) / 2;
            int y = (screenSize.height - window.getHeight()) / 2;
            window.setLocation(x, y);
            window.setVisible(true);
            window.setResizable(false);
        });
        search.setPreferredSize(new Dimension(150, 25));
        searchPanel.add(search);
        checkTitlePopup.add(searchPanel, BorderLayout.SOUTH);
        checkTitlePopup.setSize(400, 150);
        int x = (screenSize.width - checkTitlePopup.getWidth()) / 2;
        int y = (screenSize.height - checkTitlePopup.getHeight()) / 2;
        checkTitlePopup.setLocation(x, y);
        checkTitlePopup.setVisible(true);
        checkTitlePopup.setResizable(false);
    }

    private static void searchTitle() {
        JFrame p = new JFrame("Search Title");
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
        var books = searchBook(book, 0.01);
        JFrame window = new JFrame();
        window.setLayout(new BorderLayout(0, 1));

        //title sort of
        JLabel text = new JLabel(books.size() == 0 ? "Similar not titles found" : "Similar titles found:");

        text.setFont(new Font("Arial", Font.BOLD, 25));
        var textP = new JPanel();
        text.setPreferredSize(new Dimension(400, 50));
        textP.add(text);
        window.add(textP, BorderLayout.NORTH);

        JPanel rightPanel = new JPanel();
        DefaultListModel<BookListItem> listModel = new DefaultListModel<>();

        for (var item : books) {
            listModel.addElement(new BookListItem("Title: <br>" + item, "", ""));
        }
        JList<BookListItem> display = new JList<>(listModel);

        display.setCellRenderer(new BookListItemRenderer());
        display.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(display);
        scroll.setPreferredSize(new Dimension(450, 150));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        rightPanel.add(scroll);
        window.add(rightPanel, BorderLayout.CENTER);

        //button
        var okay = new JButton();
        okay.setText("OK");
        okay.addActionListener((e) -> {
            window.dispose();
        });
        var okayP = new JPanel();
        okayP.add(okay);
        window.add(okayP, BorderLayout.SOUTH);

        //JFrame settings
        window.setSize(500, 300);
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
        window.setVisible(true);
        window.setResizable(false);
    }

    private void allSimilarContent() {
        Mutable<HashMap<String, List<SimContainer>>> toShow = new Mutable<>(allSimContent(.8));
        JFrame window = new JFrame("Similar Content");
        window.setLayout(new BorderLayout(0, 1));


        JPanel list1 = new JPanel();
        DefaultListModel<BookListItem> listModel1 = new DefaultListModel<>();

        for (var item : toShow.get().keySet()) {
            int size = toShow.get().get(item).size();
            if (size > 0)
                listModel1.addElement(new BookListItem("Title: <br>" + item, "count: " + size, item));
        }
        JList<BookListItem> display1 = new JList<>(listModel1);

        display1.setCellRenderer(new BookListItemRenderer());
        display1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scroll1 = new JScrollPane(display1);
        scroll1.setPreferredSize(new Dimension(400, 550));
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        list1.add(scroll1);
        window.add(list1, BorderLayout.WEST);

        JPanel middle = new JPanel(new BorderLayout());

        JLabel text = new JLabel("Similar contents found:");
        text.setFont(new Font("Arial", Font.BOLD, 25));
        var textP = new JPanel(new BorderLayout());
        text.setPreferredSize(new Dimension(400, 50));
        textP.add(text, BorderLayout.CENTER);
        middle.add(textP, BorderLayout.NORTH);


        JPanel thrPanel = new JPanel(new BorderLayout());

        JLabel thrTxt = new JLabel("Insert a threshold:");
        thrPanel.add(thrTxt, BorderLayout.NORTH);

        JPanel thrTxtBoxPanel = new JPanel();
        JTextField thrTxtBox = new JTextField("0.8");
        thrTxtBox.setHorizontalAlignment(0);
        thrTxtBox.setPreferredSize(new Dimension(100, 25));
        thrTxtBox.setSize(new Dimension(100, 25));
        thrTxtBoxPanel.add(thrTxtBox);

        thrPanel.add(thrTxtBoxPanel, BorderLayout.CENTER);

        JPanel thrBtnPanel = new JPanel();
        JButton thrBtn = new JButton("Calculate");
        thrBtnPanel.add(thrBtn);
        thrBtn.addActionListener((e) -> {
            try {
                double thr = Double.parseDouble(thrTxtBox.getText());
                if (thr < 0 || thr > 1) throw new Exception();
                toShow.set(allSimContent(thr));
                listModel1.removeAllElements();
                if (toShow.get().size() > 0) {
                    for (var item : toShow.get().keySet()) {
                        int size = toShow.get().get(item).size();
                        if (size > 0)
                            listModel1.addElement(new BookListItem("Title: <br>" + item, "count: " + size, item));
                    }
                }
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, "The inserted value is not valid");
                exc.printStackTrace();
            }
        });

        thrTxtBoxPanel.add(thrBtnPanel);
        thrPanel.setPreferredSize(new Dimension(200, 200));
        middle.add(thrPanel, BorderLayout.CENTER);
        middle.add(new JPanel(), BorderLayout.SOUTH);

        window.add(middle, BorderLayout.CENTER);

        JPanel list2 = new JPanel();
        DefaultListModel<BookListItem> listModel2 = new DefaultListModel<>();

        JList<BookListItem> display2 = new JList<>(listModel2);

        display2.setCellRenderer(new BookListItemRenderer());
        display2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scroll2 = new JScrollPane(display2);
        scroll2.setPreferredSize(new Dimension(400, 550));
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        list2.add(scroll2);
        window.add(list2, BorderLayout.EAST);

        display1.addListSelectionListener((e) -> {
            listModel2.removeAllElements();
            try {
                for (var item : toShow.get().get(display1.getSelectedValue().name)) {
                    listModel2.addElement(new BookListItem("Title: <br>" + item.name, "Similarity: " + item.sim, item.name));
                }
            } catch (Exception exp) {
            }
        });

        //JFrame settings
        window.setSize(1200, 600);
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
        window.setVisible(true);
        window.setResizable(false);
    }

    private void allSimilarTitle() {
        Mutable<HashMap<String, List<SimContainer>>> toShow = new Mutable<>(allSimTitle(.8));
        JFrame window = new JFrame("Similar Content");
        window.setLayout(new BorderLayout(0, 1));


        JPanel list1 = new JPanel();
        DefaultListModel<BookListItem> listModel1 = new DefaultListModel<>();

        for (var item : toShow.get().keySet()) {
            int size = toShow.get().get(item).size();
            if (size > 0)
                listModel1.addElement(new BookListItem("Title: <br>" + item, "count: " + size, item));
        }
        JList<BookListItem> display1 = new JList<>(listModel1);

        display1.setCellRenderer(new BookListItemRenderer());
        display1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scroll1 = new JScrollPane(display1);
        scroll1.setPreferredSize(new Dimension(400, 550));
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        list1.add(scroll1);
        window.add(list1, BorderLayout.WEST);

        JPanel middle = new JPanel(new BorderLayout());

        JLabel text = new JLabel("Similar titles found:");
        text.setFont(new Font("Arial", Font.BOLD, 25));
        var textP = new JPanel(new BorderLayout());
        text.setPreferredSize(new Dimension(400, 50));
        textP.add(text, BorderLayout.CENTER);
        middle.add(textP, BorderLayout.NORTH);


        JPanel thrPanel = new JPanel(new BorderLayout());

        JLabel thrTxt = new JLabel("Insert a threshold:");
        thrPanel.add(thrTxt, BorderLayout.NORTH);

        JPanel thrTxtBoxPanel = new JPanel();
        JTextField thrTxtBox = new JTextField("0.8");
        thrTxtBox.setHorizontalAlignment(0);
        thrTxtBox.setPreferredSize(new Dimension(100, 25));
        thrTxtBox.setSize(new Dimension(100, 25));
        thrTxtBoxPanel.add(thrTxtBox);

        thrPanel.add(thrTxtBoxPanel, BorderLayout.CENTER);

        JPanel thrBtnPanel = new JPanel();
        JButton thrBtn = new JButton("Calculate");
        thrBtnPanel.add(thrBtn);
        thrBtn.addActionListener((e) -> {
            try {
                double thr = Double.parseDouble(thrTxtBox.getText());
                if (thr < 0 || thr > 1) throw new Exception();
                toShow.set(allSimTitle(thr));
                listModel1.removeAllElements();
                if (toShow.get().size() > 0) {
                    for (var item : toShow.get().keySet()) {
                        int size = toShow.get().get(item).size();
                        if (size > 0)
                            listModel1.addElement(new BookListItem("Title: <br>" + item, "count: " + size, item));
                    }
                }
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, "The inserted value is not valid");
                exc.printStackTrace();
            }
        });

        thrTxtBoxPanel.add(thrBtnPanel);
        thrPanel.setPreferredSize(new Dimension(200, 200));
        middle.add(thrPanel, BorderLayout.CENTER);
        middle.add(new JPanel(), BorderLayout.SOUTH);

        window.add(middle, BorderLayout.CENTER);

        JPanel list2 = new JPanel();
        DefaultListModel<BookListItem> listModel2 = new DefaultListModel<>();

        JList<BookListItem> display2 = new JList<>(listModel2);

        display2.setCellRenderer(new BookListItemRenderer());
        display2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JScrollPane scroll2 = new JScrollPane(display2);
        scroll2.setPreferredSize(new Dimension(400, 550));
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        list2.add(scroll2);
        window.add(list2, BorderLayout.EAST);

        display1.addListSelectionListener((e) -> {
            listModel2.removeAllElements();
            try {
                for (var item : toShow.get().get(display1.getSelectedValue().name)) {
                    listModel2.addElement(new BookListItem("Title: <br>" + item.name, "Similarity: " + item.sim, item.name));
                }
            } catch (Exception exp) {

            }
        });

        //JFrame settings
        window.setSize(1200, 600);
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
        window.setVisible(true);
        window.setResizable(false);
    }

    private void parsePopupDirectory() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select Directory");
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            setCurrentDirectory(fileChooser.getSelectedFile());
            JPanel p = new JPanel();
            JLabel text = new JLabel("This might take a while depending on the size of the dataset");
            p.add(text);
            final JProgressBar pr = new JProgressBar();
            pr.setStringPainted(true);
            pr.setValue(0);
            pr.setPreferredSize(new Dimension(300, 25));
            p.add(pr);
            JFrame window = new JFrame("Parsing Directory");
            window.setSize(400, 100);
            window.add(p);
            window.setVisible(true);
            parseDirectory(progress);
            final SwingWorker w = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    do {
                        try {
                            double p = progress.get();
                            pr.setValue((int) (p * 100));
                            window.repaint();
                            pr.setString((int) (p * 100) + "%");
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } while (!progress.get().equals(1.0));
                    pr.setValue((int) (100));
                    window.repaint();
                    pr.setString((int) (100) + "%");
                    updateButtonClickability();
                    window.dispose();
                    return 0;
                }
            };
            w.execute();
        }

    }

    private void saveLibrary() {
        JFileChooser fileChooser = new JFileChooser(".", FileSystemView.getFileSystemView());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select File to Save");
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                save(fileChooser.getSelectedFile());
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null, "An error occurred whilst saving");
            }
        }
    }

    private void loadLibrary() {
        JFileChooser fileChooser = new JFileChooser(".", FileSystemView.getFileSystemView());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select File to Save");
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                load(fileChooser.getSelectedFile());
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null, "An error occurred whilst saving");
            } catch (ClassNotFoundException cnfe) {
                JOptionPane.showMessageDialog(null, "The saved document doesn't match the required file format");
            }
        }
        updateButtonClickability();
    }

}

