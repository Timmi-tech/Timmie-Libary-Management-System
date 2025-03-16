package Services;

import Exceptions.DatabaseException;
import Models.Book;
import Utils.Logger;

import java.util.List;
import java.util.Scanner;

import Dao.BookDao;
import Dao.BookDaoImpl;


public class BookService {
    private static Scanner scanner = new Scanner(System.in);
    private static BookDao bookDAO = new BookDaoImpl();


    // Methods to add a book to the library
    public  static void addBook() {
    try {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        
        int copies = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter available copies: ");
                copies = Integer.parseInt(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(" Please enter a valid number for copies.");
            }
        }
        
        Book book = new Book(0, title, author, genre, copies);
        bookDAO.addBook(book);
        System.out.println(" Book added successfully!");
        Logger.log("Added Book: " + title);
    } catch (DatabaseException e) {
        System.out.println(" Error adding book: " + e.getMessage());
        Logger.log("Error in addBook method: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error in addBook method: " + e.getMessage());
    }
}
    // Methods to display all books in the library
    public static void displayBooks() {
    try {
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println(" No books available.");
        } else {
            System.out.println("\n Available Books:");
            for (Book book : books) {
                System.out.println(book);
            }
        }
    } catch (DatabaseException e) {
        System.out.println(" Error retrieving books: " + e.getMessage());
        Logger.log("Error in displayBooks method: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error in displayBooks method: " + e.getMessage());
    }
}
    // Methods to delete a book from the library
    public static void deleteBook() {
    try {
        System.out.print("Enter book ID to delete: ");
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(" Invalid book ID format. Please enter a number.");
            return;
        }
        
        // Check if book exists before deletion
        Book bookToDelete = bookDAO.getBookById(bookId);
        if (bookToDelete == null) {
            System.out.println(" Book with ID " + bookId + " not found.");
            return;
        }
        
        bookDAO.deleteBook(bookId);
        System.out.println(" Book deleted successfully!");
        Logger.log("Deleted Book ID: " + bookId);
    } catch (DatabaseException e) {
        System.out.println(" Error deleting book: " + e.getMessage());
        Logger.log("Error in deleteBook method: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error in deleteBook method: " + e.getMessage());
    }
}
    // Methods to update a book in the library
    public static void updateBook() {
    try {
        System.out.print("Enter book ID to update: ");
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(" Invalid book ID format. Please enter a number.");
            return;
        }
        
        // Check if book exists before attempting update
        Book bookToUpdate = bookDAO.getBookById(bookId);
        if (bookToUpdate == null) {
            System.out.println(" Book with ID " + bookId + " not found.");
            return;
        }
        
        // Display current book details
        System.out.println("\n Current book details:");
        System.out.println(bookToUpdate);
        
        // Get updated information
        System.out.print("Enter new title (or press Enter to keep current): ");
        String title = scanner.nextLine();
        if (!title.trim().isEmpty()) {
            bookToUpdate.setTitle(title);
        }
        
        System.out.print("Enter new author (or press Enter to keep current): ");
        String author = scanner.nextLine();
        if (!author.trim().isEmpty()) {
            bookToUpdate.setAuthor(author);
        }
        
        System.out.print("Enter new genre (or press Enter to keep current): ");
        String genre = scanner.nextLine();
        if (!genre.trim().isEmpty()) {
            bookToUpdate.setGenre(genre);
        }
        
        System.out.print("Enter new available copies (or press Enter to keep current): ");
        String copiesInput = scanner.nextLine();
        if (!copiesInput.trim().isEmpty()) {
            try {
                int copies = Integer.parseInt(copiesInput);
                bookToUpdate.setAvailableCopies(copies);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid number format. Keeping current available copies.");
            }
        }
        
        // Update the book
        bookDAO.updateBook(bookToUpdate);
        System.out.println(" Book updated successfully!");
        Logger.log("Updated Book ID: " + bookId);
        
    } catch (DatabaseException e) {
        System.out.println(" Error updating book: " + e.getMessage());
        Logger.log("Error in updateBook method: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error in updateBook method: " + e.getMessage());
    }
}
    // Methods to display a book by its ID
    public static void displayBookById() {
    try {
        System.out.print("Enter book ID to display: ");
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println(" Invalid book ID format. Please enter a number.");
            return;
        }
        
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println(" No book found with ID: " + bookId);
        } else {
            System.out.println("\n Book Details:");
            System.out.println(book);
        }
    } catch (DatabaseException e) {
        System.out.println(" Error retrieving book: " + e.getMessage());
        Logger.log("Error in displayBookById method: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error in displayBookById method: " + e.getMessage());
    }
}
    // Methods to search for a book in the library
    public static void searchBook() {
    try {
        System.out.println("\n===  Search Books ===");
        System.out.print("Enter book title, author, or genre to search: ");
        String keyword = scanner.nextLine().trim();
        
        if (keyword.isEmpty()) {
            System.out.println(" Search keyword cannot be empty");
            return;
        }
        
        System.out.print("Show only available books? (yes/no): ");
        String availableInput = scanner.nextLine().trim().toLowerCase();
        boolean onlyAvailable;
        
        if (availableInput.equals("yes") || availableInput.equals("y")) {
            onlyAvailable = true;
        } else if (availableInput.equals("no") || availableInput.equals("n")) {
            onlyAvailable = false;
        } else {
            System.out.println(" Invalid input, defaulting to showing all books");
            onlyAvailable = false;
        }
        
        System.out.println("Sort by: ");
        System.out.println("1. Title (A-Z)");
        System.out.println("2. Most Borrowed");
        System.out.println("3. Newest First");
        System.out.print("Enter choice (1-3): ");
        
        String sortChoiceInput = scanner.nextLine().trim();
        int sortChoice;
        
        try {
            sortChoice = Integer.parseInt(sortChoiceInput);
            if (sortChoice < 1 || sortChoice > 3) {
                System.out.println(" Invalid sort option, defaulting to Title");
                sortChoice = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input, defaulting to Title");
            sortChoice = 1;
        }
        
        String sortBy = switch (sortChoice) {
            case 1 -> "title";
            case 2 -> "most_borrowed";
            case 3 -> "newest";
            default -> "title"; // This is a fallback but shouldn't be reached due to validation above
        };
        
        // Display search criteria for user confirmation
        System.out.println("\nSearching for: \"" + keyword + "\"");
        System.out.println("Filter: " + (onlyAvailable ? "Available books only" : "All books"));
        System.out.println("Sort by: " + sortBy.replace("_", " "));
        
        List<Book> books = bookDAO.searchBooks(keyword, onlyAvailable, sortBy);
        
        if (books.isEmpty()) {
            System.out.println("\n No matching books found.");
        } else {
            System.out.println("\n===  Search Results (" + books.size() + " books found) ===");
            System.out.printf("%-5s | %-25s | %-20s | %-15s | %-10s\n", 
                    "ID", "TITLE", "AUTHOR", "GENRE", "AVAILABLE");
            System.out.println("-----------------------------------------------------------------------------------------------");
            
            for (Book book : books) {
                String title = book.getTitle().length() > 22 ? 
                        book.getTitle().substring(0, 19) + "..." : book.getTitle();
                String author = book.getAuthor().length() > 17 ? 
                        book.getAuthor().substring(0, 14) + "..." : book.getAuthor();
                String genre = book.getGenre().length() > 12 ? 
                        book.getGenre().substring(0, 9) + "..." : book.getGenre();
                
                System.out.printf("%-5d | %-25s | %-20s | %-15s | %-10d\n", 
                        book.getBookId(), title, author, genre, book.getAvailableCopies());
            }
        }
        
        Logger.log("Book search performed with keyword: " + keyword);
    } catch (DatabaseException e) {
        System.out.println(" Database error during search: " + e.getMessage());
        Logger.log("Error during book search: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error in book search: " + e.getMessage());
    }
}
}
