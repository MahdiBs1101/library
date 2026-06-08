package main.library.util;

import main.library.model.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    static final String BOOKS_TEXT_DIR = "data/books_text";
    static final String BOOK_LIST_CSV = "data/Book_List_text/Book_List.txt";


    /**
     * Loads all books from the CSV file and returns them as a list of Book objects.
     *
     * @return list of books loaded from the CSV file
     * @throws IOException if the CSV file cannot be read
     */
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
                    String path = bookInf[4].trim();

                    Book holder = new Book(title, path, author, publisher, year);
                    bookList.add(holder);
                } catch (NumberFormatException e) {
                    System.out.println("Error converting year to number: " + bookInf[3]);
                }
            }
        }
        return bookList;
    }


    /**
     * Saves the given list of books to the CSV file.
     * This method overwrites the existing file content.
     *
     * @param books list of books to save
     * @throws IOException if the CSV file cannot be written
     */
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
                        + holder.getTextFilePath();
                writer.write(line);
                writer.newLine();
            }
        }
    }


    /**
     * Reads a text file and splits its content into pages.
     * Each page contains at most the given number of lines.
     *
     * @param filePath path of the book text file
     * @param linesPerPage number of lines in each page
     * @return list of pages as strings
     * @throws IOException if the file cannot be read
     * @throws IllegalArgumentException if linesPerPage is zero or negative
     */
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


    /**
     * Counts the number of lines in the given text file.
     *
     * @param filePath path of the file
     * @return number of lines in the file
     * @throws IOException if the file cannot be read
     */
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


    /**
     * Writes the given text content to a file.
     * This method overwrites the existing file content.
     *
     * @param filePath path of the file
     * @param content text content to write
     * @throws IOException if the file cannot be written
     */
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


    /**
     * Reads the full text content of a file and returns it as a string.
     *
     * @param filePath path of the file
     * @return full text content of the file
     * @throws IOException if the file cannot be read
     */
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