package app;
/*
TODO 22/11/2019
 >fazer as janelas que faltam
 >linkar as janelas
 >actually pôr as funções

*/

import javax.swing.*;
import java.awt.*;

public class MainMenu {
    public static void main(String[] args) {
        Dimension dim = new Dimension(300,100); //para mudar o tamanho de cada botão
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
        file.add(a);
        a = new JMenuItem("Save library");
        file.add(a);
        a= new JMenuItem("Load library");
        file.add(a);
        a = new JMenuItem("Exit");
        file.add(a);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(file, gbc);
        tools = new JMenu("Tools");
        a = new JMenuItem("Calculate Jaccard index");
        tools.add(a);

        JMenuBar menus = new JMenuBar();
        menus.add(file);
        menus.add(tools);

        JFrame out = new JFrame ("Library Management");
        out.add(panel1);
        out.setSize(1000, 600);
        out.setVisible(true);
        out.setJMenuBar(menus);
    }
}