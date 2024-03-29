


package app;

import Tests.GlobalTests;
import modules.MinHash;
import util.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import static app.Interface.*;
import static util.Environment.contentShingleSize;


/* D:\dev\Projeto-MPEI\books\Spanish */
class GUI extends JFrame implements ActionListener {
    static Toolkit toolkit = Toolkit.getDefaultToolkit();
    static Dimension screenSize = toolkit.getScreenSize();
    private static Mutable<Double> progress = new Mutable<>(0.0);
    HashMap<String, JButton> buttons = new HashMap<>();
    HashMap<String, JPanel> panels = new HashMap<>();
    HashMap<String, JMenuItem> menuItems = new HashMap<>();
    String[] buttonsKeys = new String[]{
            "Check Title", "All Similar Title", "Check book Availability",
            "Search Title", "All Similar Content", "Request Book",
            "Add Book", "Compare 2 Books", "Return Book",
    };
    String[] fileMenuItemKeys = new String[]{"Parse Directory", "Save Library", "Load Library", "Exit"};
    String[] toolsMenuItemKeys = new String[]{"Calculate Jaccard Index", "Download Data Set", "Tests"};
    static ImageIcon img = new ImageIcon("icon.png");

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
        else if (e.getSource().equals(buttons.get("Check Title")))
            checkTitle();
        else if (e.getSource().equals(buttons.get("Request Book")))
            requestBook();
        else if (e.getSource().equals(buttons.get("Return Book")))
            returnBook();
        else if (e.getSource().equals(buttons.get("All Similar Title")))
            allSimilarTitle();
        else if (e.getSource().equals(buttons.get("Check book Availability")))
            checkBookAvailability();
        else if (e.getSource().equals(buttons.get("All Similar Content")))
            allSimilarContent();
        else if (e.getSource().equals(buttons.get("Add Book")))
            addBookMenu();
        else if (e.getSource().equals(buttons.get("Compare 2 Books")))
            compare2Books();
        else if (e.getSource().equals(menuItems.get("Parse Directory")))
            parsePopupDirectory();
        else if (e.getSource().equals(menuItems.get("Save Library")))
            saveLibrary();
        else if (e.getSource().equals(menuItems.get("Load Library")))
            loadLibrary();
        else if (e.getSource().equals(menuItems.get("Download Data Set")))
            downloadDataSetPopup();
        else if (e.getSource().equals(menuItems.get("Calculate Jaccard Index")))
            jaccardSim();
        else if (e.getSource().equals(menuItems.get("Tests")))
            tests();

    }

    private void tests() {
        JFrame frame = new JFrame("Tests");
        frame.setIconImage(img.getImage());
        JPanel outerPanel1 = new JPanel(new BorderLayout(10, 10));
        outerPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outerPanel1.add(new JLabel("Select the tests"), BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new GridLayout(5, 4));
        int col = 5;
        int row = 4;
        JPanel[][] panelHolder = new JPanel[col][row];
        setLayout(new GridLayout(col, row));
        for (int m = 0; m < col; m++) {
            for (int n = 0; n < row; n++) {
                panelHolder[m][n] = new JPanel();
                centerPanel.add(panelHolder[m][n]);
            }
        }
        String[][] testList = new String[][]{
                {"Bloom Filter", "Optimal N", "Optimal K", "False Negative", "False Positive"},
                {"Count Filter", "False Positive", "False Negative", "", ""},
                {"Hash Function", "Dispersal", "Distribution", "", ""},
                {"MinHash", "General", "<html>Optimal Number of<br>&nbsp;&nbsp;&nbsp;Hash Functions</html>", "", ""}
        };

        JCheckBox[] testListCheck = new JCheckBox[10]; // hardcoded pq é mais facil
        int testListCheckCount = 0;
        for (int i = 0; i < testList.length; i++) {
            for (int j = 0; j < testList[i].length; j++) {
                if (j == 0 || testList[i][j].equals(""))
                    panelHolder[j][i].add(new JLabel(testList[i][j]));
                else {
                    testListCheck[testListCheckCount] = new JCheckBox(testList[i][j]);
                    panelHolder[j][i].add(testListCheck[testListCheckCount++]);
                }
            }
        }
        outerPanel1.add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        JPanel paramsPanel = new JPanel(new GridLayout(2, 1));
        JTextField[] param = new JTextField[2];
        JPanel param1Panel = new JPanel();
        param1Panel.add(new JLabel("Calculate Optimal N for "));
        param[0] = new JTextField("    2");
        param1Panel.add(param[0]);
        param1Panel.add(new JLabel("% of false positives"));
        paramsPanel.add(param1Panel);


        JPanel param2Panel = new JPanel();
        param2Panel.add(new JLabel("Calculate Number of Hash Functions for "));
        param[1] = new JTextField("     3");
        param2Panel.add(param[1]);
        param2Panel.add(new JLabel("% of maximum average distance to Jaccard index"));
        paramsPanel.add(param2Panel);


        southPanel.add(paramsPanel);
        JPanel startPanel = new JPanel();
        JButton start = new JButton("Start");
        startPanel.add(start);
        southPanel.add(startPanel);

        outerPanel1.add(southPanel, BorderLayout.SOUTH);


        frame.add(outerPanel1);
        frame.setSize(new Dimension(750, 400));
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setResizable(false);
        frame.setLocation(x, y);
        frame.setVisible(true);

        start.addActionListener((e) -> {
            boolean[] options = new boolean[testListCheck.length];
            for (int i = 0; i < testListCheck.length; i++) {
                options[i] = testListCheck[i].isSelected();
            }
            JFrame console = new JFrame("Tests");

            console.setIconImage(img.getImage());
            JPanel outerPanel = new JPanel(new BorderLayout(10, 10));
            outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            outerPanel.add(new JLabel("Executing tests"), BorderLayout.NORTH);
            JTextArea jta = new JTextArea();
            JScrollPane scrll = new JScrollPane(jta);
            outerPanel.add(scrll, BorderLayout.CENTER);
            JPanel downPanel = new JPanel(new GridLayout(1, 3));

            JPanel stopPanel = new JPanel();
            JButton stop = new JButton("Stop");
            stopPanel.add(stop);
            downPanel.add(stopPanel);
            outerPanel.add(downPanel, BorderLayout.SOUTH);
            console.add(outerPanel);
            console.setSize(new Dimension(650, 400));
            int x2 = (screenSize.width - console.getWidth()) / 2;
            int y2 = (screenSize.height - console.getHeight()) / 2;
            console.setResizable(false);
            console.setLocation(x2, y2);
            console.setVisible(true);

            double[] params = new double[2];
            for (int i = 0; i < 2; i++) {
                try {
                    params[i] = Double.parseDouble(param[i].getText()) / 100;
                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(this, "An error occurred while parsing the inputed value,\n" +
                            "Default values will be used instead");
                    params[i] = i == 0 ? 0.1 : 0.02;
                }
            }

            Thread tests = (new GlobalTests(jta, 2500, options, params));
            tests.start();
            stop.addActionListener((e2) -> {
                tests.stop();
            });
            console.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    tests.stop();
                }
            });
        });
    }


    private GUI(String windowHeader) {
        super(windowHeader);
        this.setIconImage(img.getImage());
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
        System.out.println(Runtime.getRuntime().availableProcessors());
        (new GUI("Library Manager")).setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                TimeThis.printAllDelays();
            }
        });
    }

    private void returnBook() {
        JFrame p = new JFrame("Return Book");
        p.setIconImage(img.getImage());
        p.setLayout(new BorderLayout());
        p.setIconImage(img.getImage());
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

    private void returnedBook(String book) {
        String s = "";
        if (Interface.returnBook(book))
            s = "Book returned";
        else
            s = "Book not returned";
        JOptionPane.showMessageDialog(this, s);
    }


    private void checkBookAvailability() {
        JFrame p = new JFrame("Check Book Availability");
        p.setIconImage(img.getImage());
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
        search.setText("Check");
        search.addActionListener((e) -> {
            String book = title.getText();
            String s = "";
            if (Interface.isAvailable(book))
                s = "Book is available for request";
            else
                s = "Book is not available for request";
            JOptionPane.showMessageDialog(this, s);

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

    private void requestBook() {
        JFrame p = new JFrame("Request Book");
        p.setIconImage(img.getImage());
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
            String s = "";
            if (Interface.requestBook(book))
                s = "Book requested";
            else
                s = "Book not requested";
            JOptionPane.showMessageDialog(this, s);

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

    private static void checkTitle() {
        JFrame checkTitlePopup = new JFrame("Check Title");
        checkTitlePopup.setIconImage(img.getImage());
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
            window.setIconImage(img.getImage());
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
        p.setIconImage(img.getImage());
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

    private void compare2Books() {
        JFrame p = new JFrame("Compare 2 Books");
        p.setIconImage(img.getImage());
        p.setLayout(new BorderLayout());
        //header
        JLabel text = new JLabel();
        text.setText("Select 2 books to compare");
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        text.setFont(new Font("Arial", Font.BOLD, 25));
        text.setHorizontalAlignment(0);
        textPanel.add(text, BorderLayout.CENTER);
        textPanel.setPreferredSize(new Dimension(400, 50));
        p.add(textPanel, BorderLayout.NORTH);

        //textfield
        JPanel insertPanel = new JPanel();
        JComboBox<ProcessedBooksResult> books1 = new JComboBox<>();
        JComboBox<ProcessedBooksResult> books2 = new JComboBox<>();
        for (var book : getAvailableBooks().values()) {
            books1.addItem(book);
            books2.addItem(book);
        }
        books1.setPreferredSize(new Dimension(130, 30));
        books2.setPreferredSize(new Dimension(130, 30));
        insertPanel.add(books1, BorderLayout.WEST);
        insertPanel.add(books2, BorderLayout.EAST);
        insertPanel.setPreferredSize(new Dimension(310, 50));
        p.add(insertPanel, BorderLayout.CENTER);

        //button
        JPanel comparePanel = new JPanel();
        JButton compare = new JButton();
        compare.setText("Compare");
        compare.addActionListener((e) -> {
            JOptionPane.showMessageDialog(this, "<html>The similarity between 2 books is " +
                    ((ProcessedBooksResult) books1.getSelectedItem()).minHashedContent.calcSimTo(
                            ((ProcessedBooksResult) books2.getSelectedItem()).minHashedContent
                    ) + "</html>");
        });
        compare.setPreferredSize(new Dimension(150, 25));
        comparePanel.add(compare);
        p.add(comparePanel, BorderLayout.SOUTH);


        p.setSize(400, 175);
        int x = (screenSize.width - p.getWidth()) / 2;
        int y = (screenSize.height - p.getHeight()) / 2;
        p.setLocation(x, y);
        p.setVisible(true);
        p.setResizable(false);
    }

    private static void findSimilarTitle(String book) {
        var books = searchBook(book, 0.01);
        JFrame window = new JFrame("Find Similar Title");
        window.setIconImage(img.getImage());
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

        boolean lshUsed = bookStockHashes.size() > 2000;
        Mutable<HashMap<String, List<SimContainer>>> toShow = new Mutable<>(allSimContent(.8));
        JFrame window = new JFrame("Similar Content");
        window.setIconImage(img.getImage());
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
        thrTxt.setEnabled(!lshUsed);
        thrPanel.add(thrTxt, BorderLayout.NORTH);

        JPanel thrTxtBoxPanel = new JPanel();
        JTextField thrTxtBox = new JTextField("0.8");
        thrTxtBox.setEnabled(!lshUsed);

        thrTxtBox.setHorizontalAlignment(0);
        thrTxtBox.setPreferredSize(new Dimension(100, 25));
        thrTxtBox.setSize(new Dimension(100, 25));
        thrTxtBoxPanel.add(thrTxtBox);

        thrPanel.add(thrTxtBoxPanel, BorderLayout.CENTER);

        JPanel thrBtnPanel = new JPanel();
        JButton thrBtn = new JButton("Calculate");
        thrBtn.setEnabled(!lshUsed);

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
                JOptionPane.showMessageDialog(this, "The inserted value is not valid");
                exc.printStackTrace();
            }
        });

        thrTxtBoxPanel.add(thrBtnPanel);
        if (lshUsed)
            thrTxtBoxPanel.add(new JLabel("Threshold definition not available because the set is to large and therefore " +
                    "LSHVariant optimization was used"));
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
        boolean lshUsed = bookStockHashes.size() > 2000;
        Mutable<HashMap<String, List<SimContainer>>> toShow = new Mutable<>(allSimTitle(.8));
        JFrame window = new JFrame("Similar Content");
        window.setIconImage(img.getImage());
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
        thrTxt.setEnabled(!lshUsed);
        thrPanel.add(thrTxt, BorderLayout.NORTH);

        JPanel thrTxtBoxPanel = new JPanel();
        JTextField thrTxtBox = new JTextField("0.8");
        thrTxtBox.setHorizontalAlignment(0);
        thrTxtBox.setPreferredSize(new Dimension(100, 25));
        thrTxtBox.setSize(new Dimension(100, 25));
        thrTxtBoxPanel.add(thrTxtBox);
        thrTxtBox.setEnabled(!lshUsed);
        thrPanel.add(thrTxtBoxPanel, BorderLayout.CENTER);

        JPanel thrBtnPanel = new JPanel();
        JButton thrBtn = new JButton("Calculate");
        thrBtn.setEnabled(!lshUsed);
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
                JOptionPane.showMessageDialog(this, "The inserted value is not valid");
                exc.printStackTrace();
            }
        });

        thrTxtBoxPanel.add(thrBtnPanel);
        if (lshUsed)
            thrTxtBoxPanel.add(new JLabel("Threshold definition not available because the set is to large and therefore " +
                    "LSHVariant optimization was used"));
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


    private void jaccardSim() {
        JOptionPane.showMessageDialog(this, "Select 2 files to compare");
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select Directory");
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            JFileChooser fileChooser2 = new JFileChooser(".");
            fileChooser2.setAcceptAllFileFilterUsed(false);
            fileChooser2.setDialogTitle("Select Directory");
            int option2 = fileChooser2.showOpenDialog(this);
            if (option2 == JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "The Jaccard similarity between 2 files is " +
                        MinHash.jaccardIndex(fileChooser.getSelectedFile(), fileChooser2.getSelectedFile(), contentShingleSize));
            }

        }
    }

    private void parsePopupDirectory() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select Directory");
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            JPanel p = new JPanel();
            JLabel text = new JLabel("This might take a while depending on the size of the dataset");
            p.add(text);
            final JProgressBar pr = new JProgressBar();
            pr.setStringPainted(true);
            pr.setValue(0);
            pr.setPreferredSize(new Dimension(300, 25));
            p.add(pr);
            JFrame window = new JFrame("Parsing Directory");
            window.setIconImage(img.getImage());
            window.setIconImage(img.getImage());
            window.setSize(400, 100);
            window.add(p);
            window.setLocationRelativeTo(this);
            window.setVisible(true);
            progress.set(0.0);
            parseDirectory(fileChooser.getSelectedFile(), progress);
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

    private void downloadDataSetPopup() {
        JFrame qttFrame = new JFrame("Download Dataset");
        qttFrame.setIconImage(img.getImage());
        JPanel outerPanel = new JPanel(new GridLayout(2, 1));
        qttFrame.setSize(new Dimension(600, 125));
        JPanel p1 = new JPanel();
        JLabel label = new JLabel("<html>Insert approximate maximum of books to download (this number is merely indicative)<br></html>");
        p1.add(label);
        JPanel p2 = new JPanel();
        JTextField input = new JTextField("2500");
        input.setPreferredSize(new Dimension(100, 25));
        JButton btnInput = new JButton("Download");
        p2.add(input);
        p2.add(btnInput);
        outerPanel.add(p1);
        outerPanel.add(p2);
        qttFrame.add(outerPanel);
        qttFrame.setLocationRelativeTo(this);
        qttFrame.setVisible(true);
        btnInput.addActionListener((e) -> {
            final int numberOfBooks;
            try {
                numberOfBooks = Integer.parseInt(input.getText());
                if (numberOfBooks <= 0) throw new Exception();
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(this, "Insert a valid quantity");
                return;
            }
            qttFrame.dispose();
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setDialogTitle("Select Directory");
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                JPanel p = new JPanel();
                JLabel text = new JLabel("This might take a while depending on the size of the dataset");
                p.add(text);
                final JProgressBar pr = new JProgressBar();
                pr.setStringPainted(true);
                pr.setValue(0);
                pr.setPreferredSize(new Dimension(300, 25));
                p.add(pr);
                JFrame window = new JFrame("Downloading Dataset");
                window.setIconImage(img.getImage());
                window.setSize(400, 100);
                window.add(p);
                window.setLocationRelativeTo(this);
                window.setVisible(true);
                progress.set(0.0);
                downloadTestData(fileChooser.getSelectedFile(), numberOfBooks, progress);
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
        });

    }

    private void addBookMenu() {
        JOptionPane.showMessageDialog(this, "Select a book to add");
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Select Book");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Book (.txt)", "txt");
        fileChooser.setFileFilter(filter);
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            if (addBook(fileChooser.getSelectedFile())) {
                JOptionPane.showMessageDialog(this, "Book added");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Book not added");

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
                JOptionPane.showMessageDialog(this, "An error occurred whilst saving");
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
                JOptionPane.showMessageDialog(this, "An error occurred whilst loading");
            } catch (ClassNotFoundException cnfe) {
                JOptionPane.showMessageDialog(this, "The saved document doesn't match the required file format");
            }
        }
        updateButtonClickability();
    }

}

