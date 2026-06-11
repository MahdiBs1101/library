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

    public void createBooksPanel() {

        booksPanel.removeAll() ;
        booksPanel.setLayout(new GridLayout(0,1)) ;

        List<Book> books = service.getAllBooks();


        for (Book b : books) {

            JButton bookButton = new JButton(b.getTitle());

            bookButton.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectedBook = b;

                            try {
                                showMenuBook();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }
                    }
            );

            booksPanel.add(bookButton) ;
        }

        JButton refresh = new JButton("Refresh");

        refresh.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        booksPanel.removeAll() ;
                        createBooksPanel() ;
                        booksPanel.revalidate() ;
                        booksPanel.repaint() ;
                    }
                }
        );

        booksPanel.add(refresh) ;
    }

    public void showMenuBook() throws IOException {

        menuPanel.removeAll() ;
        menuPanel.setLayout(new GridLayout(0,1)) ;

        titleField = new JTextField(selectedBook.getTitle()) ;
        authorField = new JTextField(selectedBook.getAuthor()) ;
        publisherField = new JTextField(selectedBook.getPublisher()) ;
        yearField = new JTextField(Integer.toString(selectedBook.getPublicationYear())) ;

        JLabel countLine = new JLabel("Lines count :" + service.countLines(selectedBook)) ;

        JButton saveData = new JButton("Save Metadata") ;

        saveData.addActionListener(

                new ActionListener() {

                    @Override
                    public void actionPerformed (ActionEvent e) {

                        try {

                            String title = titleField.getText();
                            String author = authorField.getText();
                            String publisher = publisherField.getText();
                            int year = Integer.parseInt(yearField.getText());

                            boolean result = service.editBookMetaData(selectedBook.getId(), title, author, publisher, year) ;

                            if (result) JOptionPane.showMessageDialog(MainFrame.this, "Saved Successfully") ;

                            else JOptionPane.showMessageDialog(MainFrame.this, "Error") ;

                        }
                        catch (NumberFormatException ee) {
                            JOptionPane.showMessageDialog(MainFrame.this,"Invalid year") ;
                        }
                    }



                }

        );

        JButton readBook = new JButton("Read Book") ;

        readBook.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        openBook(false);
                    }
                }
        );

        JButton editBook = new JButton("Edit Book") ;

        editBook.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        openBook(true) ;
                    }
                }
        );

        JButton back = new JButton("Back") ;

        back.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        getContentPane().removeAll() ;
                        createBooksPanel() ;
                        add(booksPanel) ;
                        revalidate() ;
                        repaint() ;
                    }
                }
        );

        menuPanel.add(titleField);
        menuPanel.add(authorField);
        menuPanel.add(publisherField);
        menuPanel.add(yearField);
        menuPanel.add(countLine);

        menuPanel.add(saveData);
        menuPanel.add(readBook);
        menuPanel.add(editBook);
        menuPanel.add(back);

        getContentPane().removeAll();

        add(menuPanel);

        revalidate();
        repaint();
    }

    public void openBook(boolean editable){

        readerPanel.removeAll() ;
        readerPanel.setLayout(new BorderLayout()) ;

        pageArea.setEditable(editable) ;

        pages = service.getBookPages(selectedBook,3) ;

        currentPage = 0 ;

        pageArea.setLineWrap(true) ;
        pageArea.setWrapStyleWord(true) ;

        if(!pages.isEmpty()){
            pageArea.setText(pages.get(0)) ;
        }

        pageInfoLabel.setText("Page " + (currentPage + 1) + " / " + pages.size());

        JButton previousPage = new JButton("<") ;

        previousPage.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        pages.set(currentPage,pageArea.getText()) ;

                        if(currentPage>0){
                            --currentPage;
                            pageArea.setText(pages.get(currentPage));

                            pageInfoLabel.setText("Page " + (currentPage + 1) + " / " + pages.size()) ;
                        }
                    }
                }
        );

        JButton nextPage = new JButton(">") ;

        nextPage.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        pages.set(currentPage,pageArea.getText()) ;

                        if(currentPage < pages.size()-1){
                            ++currentPage;
                            pageArea.setText(pages.get(currentPage));

                            pageInfoLabel.setText("Page " + (currentPage + 1) + " / " + pages.size()) ;
                        }
                    }
                }
        );

        JPanel buttonPanel = new JPanel() ;

        buttonPanel.add(previousPage) ;
        buttonPanel.add(pageInfoLabel) ;
        buttonPanel.add(nextPage) ;

        if(editable){

            JButton apply = new JButton("Apply") ;

            apply.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            pages.set(currentPage,pageArea.getText()) ;

                            String content = "" ;

                            for(String page : pages){
                                content += page ;

                            }

                            boolean result = service.editBookContent(selectedBook.getId(),content) ;

                            if(result){
                                JOptionPane.showMessageDialog(MainFrame.this,"Saved successfully") ;
                            }
                            else{
                                JOptionPane.showMessageDialog(MainFrame.this,"Error saving") ;
                            }
                        }
                    }
            );

            buttonPanel.add(apply) ;
        }

        JButton back = new JButton("Back") ;
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showMenuBook() ;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        buttonPanel.add(back) ;

        readerPanel.add(new JScrollPane(pageArea),BorderLayout.CENTER) ;
        readerPanel.add(buttonPanel,BorderLayout.SOUTH) ;

        getContentPane().removeAll() ;
        add(readerPanel) ;

        revalidate() ;
        repaint() ;
    }

}