package main.library.util;

import main.library.model.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    static final String BOOKS_TEXT_DIR = "data/books_text";
    static final String BOOK_LIST_CSV = "data/Book_List_text/Book_List.txt";


    public static List<Book> loadBooksFromCSV() throws IOException {
        ArrayList<Book> bookList = new ArrayList<>();

        try(BufferedReader reader =
                    new BufferedReader(new FileReader(BOOK_LIST_CSV))) {
            String line;
            line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] bookInf = line.split(",");

                if (bookInf.length < 5) {
                    System.out.println("Invalid line in CSV: " + line);
                    continue;
                }

                try {
                    String title = bookInf[0].trim();
                    String author = bookInf[1].trim();
                    String publisher = bookInf[2].trim();
                    int year = Integer.parseInt(bookInf[3].trim());
                    String Path = bookInf[4].trim();

                    Book holder = new Book(title, Path, author, publisher, year);
                    bookList.add(holder);
                } catch (NumberFormatException e) {
                    System.out.println("Error converting year to number: " + bookInf[3]);
                }
            }
        }
        return bookList;
    }

    

}