package main.library.service;

import main.library.model.Book;

import java.io.IOException ;

import java.util.* ;

import main.library.util.FileManager ;

public class LibraryService {

    private List<Book> books ;

    public LibraryService() throws IOException {

        books = FileManager.loadBooksFromCSV() ;
        FileManager.saveBooksToCSV(books) ;
    }

    public List<Book> getAllBooks(){
        return new ArrayList<>(books) ;
    }

    public Optional<Book> findBookById(int id){


        Optional <Book> selectedBook = Optional.empty() ;
        
        for(Book b : books){

            if(b.getId()==id){
                selectedBook = Optional.of(b);
                break ;

            }
        }
        return selectedBook ;
    }

    public void saveBooks() throws IOException {

        FileManager.saveBooksToCSV(books) ;
    }

    public boolean editBookMetaData(int bookId,String newTitle,String newAuthor,String newPublisher, Integer newYear){

        Book selectedBook = findBookById(bookId).get() ;

        if(selectedBook==null) return false ;

        if(selectedBook.getTitle() != null) selectedBook.setTitle(newTitle) ;

        if(selectedBook.getAuthor() != null) selectedBook.setAuthor(newAuthor) ;

        if(selectedBook.getPublisher() != null) selectedBook.setPublisher(newPublisher);

        if(selectedBook.getPublicationYear() != 0) selectedBook.setPublicationYear(newYear) ; ;

        return true ;
    }

    public List<String> getBookPages(Book book,int linesPerPage) throws IOException {

        return FileManager.readBookPages(book.getTextFilePath(),linesPerPage) ;
    }

    public String readFullText(int bookId) throws IOException {

        Book selectedBook = findBookById(bookId).get() ;
         return FileManager.readFullText(selectedBook.getTextFilePath()) ;
    }

    public boolean editBookContent(int bookId, String newContent) throws IOException {

        Book selectedBook = findBookById(bookId).get() ;
        if(selectedBook==null) return false ;

        FileManager.writeBookText(selectedBook.getTextFilePath(),newContent) ;
        return true ;
    }

    public int countLines(Book book) throws IOException {

        return FileManager.countLines(book.getTextFilePath()) ;
    }

}
