package app;

import util.ProcessedBooksResult;

import javax.swing.*;
import java.awt.*;

public class BookListItem extends JPanel implements ListCellRenderer<ProcessedBooksResult> {
    @Override
    public Component getListCellRendererComponent(
            JList<? extends ProcessedBooksResult> list,
            ProcessedBooksResult value, int index,
            boolean isSelected, boolean cellHasFocus) {
        removeAll();
        setBackground(Color.WHITE);
        if(isSelected)
            setBackground(new Color(150,150,255)) ;
        add(new JLabel(value.name));
        return this;
    }
}
