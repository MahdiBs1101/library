package main.library.service;

import java.io.IOException ;
import java.util.* ;
import main.library.model.Book;
import main.library.util.FileManager ;

/**
 * LibraryService is the main service class that handles operations on books.
 * It provides methods to read, write, edit, and retrieve book data.
 */
public class LibraryService {
    private List<Book> books;


    public LibraryService() {
        try {
            books = FileManager.loadBooksFromCSV();
            FileManager.saveBooksToCSV(books);
        } catch (IOException e) {
            System.out.println("Error loading or saving books: " + e.getMessage());
            books = new ArrayList<>();
        }
    }


    // Returns a copy of all books.
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }


    // Finds a book by its ID.
    public Optional<Book> findBookById(int id) {
        Optional<Book> selectedBook = Optional.empty();

        for (Book b : books) {
            if (b.getId() == id) {
                selectedBook = Optional.of(b);
                break;
            }
        }

        return selectedBook;
    }


    // Saves the current list of books to the CSV file.
    public void saveBooks() {
        try {
            FileManager.saveBooksToCSV(books);
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }


    // Edits the metadata (title, author, publisher, year) of a book.
    public boolean editBookMetaData(int bookId, String newTitle, String newAuthor,
                                    String newPublisher, Integer newYear) {
        Optional<Book> result = findBookById(bookId);
        if (result.isEmpty()) return false;

        Book selectedBook = result.get();

        if (newTitle != null && !newTitle.trim().isEmpty()) {
            selectedBook.setTitle(newTitle.trim());
        }
        if (newAuthor != null && !newAuthor.trim().isEmpty()) {
            selectedBook.setAuthor(newAuthor.trim());
        }
        if (newPublisher != null && !newPublisher.trim().isEmpty()) {
            selectedBook.setPublisher(newPublisher.trim());
        }
        if (newYear != null && newYear > 0) {
            selectedBook.setPublicationYear(newYear);
        }

        saveBooks();

        return true;
    }


    // Retrieves book pages based on the given lines per page.
    public List<String> getBookPages(Book book, int linesPerPage) {
        if (book == null) return new ArrayList<>();

        try {
            return FileManager.readBookPages(book.getTextFilePath(), linesPerPage);
        } catch (IOException e) {
            System.out.println("Error loading book: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }


    // Reads the full text of a book.
    public String readFullText(int bookId) {
        Optional<Book> result = findBookById(bookId);
        if (result.isEmpty()) return "";

        Book selectedBook = result.get();

        try {
            return FileManager.readFullText(selectedBook.getTextFilePath());
        } catch (IOException e) {
            System.out.println("Error loading book: " + e.getMessage());
        }
        return "";
    }


    // Edits the full content of a book.
    public boolean editBookContent(int bookId, String newContent) {
        if (newContent == null) return false;

        Optional<Book> result = findBookById(bookId);
        if (result.isEmpty()) return false;

        Book selectedBook = result.get();

        try {
            FileManager.writeBookText(selectedBook.getTextFilePath(), newContent);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving book content: " + e.getMessage());
        }

        return false;
    }



    // Counts the number of lines in a book's text file.
    public int countLines(Book book) {
        if (book == null) return 0;

        try {
            return FileManager.countLines(book.getTextFilePath());
        } catch (IOException e) {
            System.out.println("Error loading book: " + e.getMessage());
        }
        return 0;
    }
}
