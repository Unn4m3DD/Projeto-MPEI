package app;

import util.ProcessedBooksResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BookListItemRenderer extends JPanel implements ListCellRenderer<BookListItem> {
    @Override
    public Component getListCellRendererComponent(
            JList<? extends BookListItem> list,
            BookListItem value, int index,
            boolean isSelected, boolean cellHasFocus) {
        removeAll();
        setBackground(Color.WHITE);
        if(isSelected)
            setBackground(new Color(150,150,255)) ;
        setLayout(new BorderLayout());

        JLabel left = new JLabel("<html> " + value.left + " <br> </html>");
        left.setPreferredSize(new Dimension(350,70));
//        left.setLineWrap(true);
//        left.setWrapStyleWord(true);
        left.setBorder(new EmptyBorder(0, 5, 5, 5));;
        add(left , BorderLayout.PAGE_START);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(new JLabel(value.right));
        JPanel rightPanel2 = new JPanel(new BorderLayout());
        rightPanel2.add(rightPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(350,100));
        return this;
    }
}


