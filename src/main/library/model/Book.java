

public class Book {
    private final int id;
    private String title;
    private String textFilePath;
    private String author;
    private String publisher;
    private int publicationYear;
    private static int totalBooks = 0;

    public Book(String title, String textFilePath, String author,
                String publisher, int publicationYear) {
        id = totalBooks++;
        this.title = title;
        this.textFilePath = textFilePath;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
    }



    @Override
    public String toString() {
        return "[" + id +"] " + title + "  |  " +
                author + "  |  " + publisher + "  |  " +
                publicationYear;
    }

    // getter and setter

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextFilePath() {
        return textFilePath;
    }

    public void setTextFilePath(String textFilePath) {
        this.textFilePath = textFilePath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

}