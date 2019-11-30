package app;

import util.ProcessedBooksResult;

import javax.swing.*;
import java.awt.*;

public class BookListItem extends JToggleButton {
    ProcessedBooksResult book;
    public BookListItem(ProcessedBooksResult book) {
        this.book = book;
        add(new Label(book.name));
    }
}
