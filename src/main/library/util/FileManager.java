package main.library.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import main.library.model.Book;


/**
 * FileManager handles reading and writing book data and text content.
 * Provides utilities to load/save books from CSV, read/write book text,
 * split text into pages, and count lines.
 */
public class FileManager {
    private static final String BOOKS_TEXT_DIR = "data/books_text/";
    private static final String BOOK_LIST_CSV = "data/Book_List_text/Book_List.txt";


    // Loads all books from the CSV file and returns them as a list of Book objects.
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
                    String path = BOOKS_TEXT_DIR + bookInf[4].trim();

                    Book holder = new Book(title, path, author, publisher, year);
                    bookList.add(holder);
                } catch (NumberFormatException e) {
                    System.out.println("Error converting year to number: " + bookInf[3]);
                }
            }
        }
        return bookList;
    }


    // Saves the given list of books to the CSV file.
    public static void saveBooksToCSV(List<Book> books) throws IOException {
        try(BufferedWriter writer =
                    new BufferedWriter(new FileWriter(BOOK_LIST_CSV))) {
            writer.write("Title,Author,Publisher,Publication Year,Path");
            writer.newLine();
            for(Book holder : books) {
                String line = holder.getTitle() + ","
                        + holder.getAuthor() + ","
                        + holder.getPublisher() + ","
                        + holder.getPublicationYear() + ","
                        + holder.getTextFilePath().substring(BOOKS_TEXT_DIR.length()  + 1);
                writer.write(line);
                writer.newLine();
            }
        }
    }


    // Reads a text file and splits its content into pages.
    public static List<String> readBookPages(String filePath, int linesPerPage) throws IOException {
        if (linesPerPage <= 0) {
            throw new IllegalArgumentException("linesPerPage must be positive");
        }

        ArrayList<String> pagesOfBook = new ArrayList<>();

        try(BufferedReader reader =
                    new BufferedReader(new FileReader(filePath))) {
            boolean isFinished = false;

            while(true) {
                StringBuilder page = new StringBuilder();
                for (int i = 0; i < linesPerPage; i++) {
                    String line = reader.readLine();

                    if (line == null) {
                        isFinished = true;
                        break;
                    }

                    page.append(line).append("\n");
                }

                if (!page.isEmpty()) pagesOfBook.add(page.toString());
                if (isFinished) break;
            }
        }
        return pagesOfBook;
    }


    // Counts the number of lines in the given text file.
    public static int countLines(String filePath) throws IOException {
        int lines = 0;
        try(BufferedReader reader =
                new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                lines++;
            }
        }
        return lines;
    }



    // Writes the given text content to a file.
    public static void writeBookText(String filePath, String content) throws IOException {
        try(BufferedWriter writer =
                    new BufferedWriter(new FileWriter(filePath))) {
            String[] lines = content.split("\\R");
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }



    // Reads the full text content of a file and returns it as a string.
    public static String readFullText(String filePath) throws IOException {
        try(BufferedReader reader =
                    new BufferedReader(new FileReader(filePath))) {
            StringBuilder fullText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fullText.append(line).append("\n");
            }
            return fullText.toString();
        }
    }
}