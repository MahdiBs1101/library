package main.library.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.* ;
import java.util.List;

import main.library.model.Book ;
import main.library.service.LibraryService ;

public class MainFrame extends JFrame {

    private LibraryService service ;

    private JPanel booksPanel = new JPanel() ;
    private JPanel menuPanel = new JPanel() ;
    private JPanel readerPanel = new JPanel() ;

    private Book selectedBook ;

    private JTextField titleField  ;
    private JTextField authorField ;
    private JTextField publisherField ;
    private JTextField yearField ;

    private JTextArea pageArea = new JTextArea() ;

    private List<String> pages ;
    private int currentPage  ;

    private JLabel pageInfoLabel = new JLabel() ;


    public MainFrame() throws IOException {

        service = new LibraryService() ;
        setTitle("Library") ;
        setSize(900,700) ;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) ;

        createBooksPanel() ;
        add(booksPanel) ;

        setVisible(true) ;
    }


}