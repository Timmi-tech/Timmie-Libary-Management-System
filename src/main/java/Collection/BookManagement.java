package Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;
import Utils.Logger;
import Models.Book;

public class BookManagement {
    public Library library;  // Reference to Library

    public BookManagement(Library library) {
        this.library = library;
    }
     // Add a new book to the library
    public Book addBook(String title, String author, String genre, int copies) {
        int bookId = library.nextBookId++;  // Access nextBookId from Library
        Book book = new Book(bookId, title, author, genre, copies);
        library.books.add(book);  // Add to Library's book list
        Logger.log("Added new book: " + book.getTitle());
        System.out.println(" Book added successfully!");
        return book;
    }
     // Update book details
    public boolean updateBook(int bookId, String title, String author, String genre, int copies) {
        for (Book book : library.books) { // Access books from Library
            if (book.getBookId() == bookId) {
                book.setTitle(title);
                book.setAuthor(author);
                book.setGenre(genre);
                book.setAvailableCopies(copies);
                Logger.log("Updated book: " + book.getTitle());
                return true;
            }
        }
        return false;
    }
    // Delete a book from the library
    public boolean deleteBook(int bookId) {
        if (library.borrowedBooks.containsKey(bookId)) {  // Check borrowedBooks from Library
            return false; // Can't delete a borrowed book
        }

        for (Iterator<Book> iterator = library.books.iterator(); iterator.hasNext();) {
            Book book = iterator.next();
            if (book.getBookId() == bookId) {
                iterator.remove();
                Logger.log("Deleted book with ID: " + bookId);
                return true;
            }
        }
        return false;
    }
    // Search for books by title
    public Book searchBookByTitle(String title) {
        List<Book> sortedBooks = new ArrayList<>(library.books);
        Collections.sort(sortedBooks, Comparator.comparing(Book::getTitle)); // Sort by title

        int index = Collections.binarySearch(sortedBooks, new Book(0, title, "", "", 0),
                Comparator.comparing(Book::getTitle));

        return (index >= 0) ? sortedBooks.get(index) : null; // Return book if found
    }
     // Search for a book by author using binary search
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (Book book : library.books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                results.add(book);
            }
        }
        return results;
    }
    // Get all books sorted by title
    public List<Book> getBooksSortedByTitle() {
        List<Book> sortedBooks = new ArrayList<>(library.books);
        Collections.sort(sortedBooks, Comparator.comparing(Book::getTitle));
        return sortedBooks;
    }
    // Get all books sorted by author
    public List<Book> getBooksSortedByAuthor() {
        List<Book> sortedBooks = new ArrayList<>(library.books);
        Collections.sort(sortedBooks, Comparator.comparing(Book::getAuthor));
        return sortedBooks;
    }
    // Get all books sorted by genre
    public List<Book> getBooksSortedByGenre() {
        List<Book> sortedBooks = new ArrayList<>(library.books);
        Collections.sort(sortedBooks, Comparator.comparing(Book::getGenre));
        return sortedBooks;
    }
     // Get all books in the library
    public List<Book> getAllBooks() {
        return new ArrayList<>(library.books);
    }
}
