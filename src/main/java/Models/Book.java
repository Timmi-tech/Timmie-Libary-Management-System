package Models;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private int availableCopies;

    // Constructor
    public Book(int bookId, String title, String author, String genre, int availableCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availableCopies = availableCopies;
    }

    // Constructor without ID for new books
    public Book(String title, String author, String genre, int availableCopies) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.availableCopies = availableCopies;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    @Override
    public String toString() {
        return String.format(
        "📖 Book Details:\n" +
        "--------------------------\n" +
        "📌 ID            : %d\n" +
        "📚 Title         : %s\n" +
        "✍️  Author        : %s\n" +
        "🎭 Genre         : %s\n" +
        "📦 Available     : %d copies\n" +
        "--------------------------",
        bookId, title, author, genre, availableCopies
    );
}

}
