package Collection.Menu;

import java.util.List;
import java.util.Scanner;

import Collection.BookManagement;
import Collection.Borrowingmanagement; 
import Collection.Library;
import Collection.MemberManagement;
import Models.Book;
import Models.BorrowingRecord;
import Models.Member;


public class LibraryMenu {
    private Scanner scanner;
    private Library library;
    private BookManagement bookManager;
    private MemberManagement memberManager;
    private Borrowingmanagement borrowingManager;

    public LibraryMenu() {
        scanner = new Scanner(System.in);
        library = new Library();
        bookManager = new BookManagement(library);
        memberManager = new MemberManagement(library);
        borrowingManager = new Borrowingmanagement(library);
        
        // Add some sample data for testing
        addSampleData();
    }

    private void addSampleData() {
        // Add some sample books
        bookManager.addBook("The Stupid Gatsby", "F. tomiwa scott", "Fictionl", 1);
        bookManager.addBook("To Kill a Mockingbird", "Harper cooper", "Modest", 10);
        
        // Add some sample members
        memberManager.addMember("tomiwa ope", "tom@gmail.com", "08012345678");
        memberManager.addMember("ope adeyemi", "opeyemi@gmail.com", "09098765432");
    }

    public void start() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Book Management");
            System.out.println("2. Member Management");
            System.out.println("3. Borrowing Management");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    showBookMenu();
                    break;
                case 2:
                    showMemberMenu();
                    break;
                case 3:
                    showBorrowingMenu();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Thank you for using the Library Management System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        
        scanner.close();
    }

    private void showBookMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== BOOK MANAGEMENT =====");
            System.out.println("1. Add a new book");
            System.out.println("2. Update a book");
            System.out.println("3. Delete a book");
            System.out.println("4. Search book by title");
            System.out.println("5. Search books by author");
            System.out.println("6. sort books by title");
            System.out.println("7. sort books by author");
            System.out.println("8. sort books by genre");
            System.out.println("9. View all books");
            System.out.println("10. Back to main menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    updateBook();
                    break;
                case 3:
                    deleteBook();
                    break;
                case 4:
                    searchBookByTitle();
                    break;
                case 5:
                    searchBooksByAuthor();
                    break;
                case 6:
                    sortBooksByTitle();
                    break;
                case 7:
                    sortBooksByAuthor();
                    break;
                case 8:
                    sortBooksByGenre();
                    break;
                case 9:
                    viewAllBooks();
                    break;
                case 10:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addBook() {
        System.out.println("\n----- Add a New Book -----");
        scanner.nextLine(); // Clear the buffer
        
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        
        System.out.print("Enter number of copies: ");
        int copies = getIntInput();
        
        bookManager.addBook(title, author, genre, copies);
    }

    private void updateBook() {
        System.out.println("\n----- Update a Book -----");
        viewAllBooks();
        
        System.out.print("Enter the ID of the book to update: ");
        int bookId = getIntInput();
        
        Book book = null;
        for (Book b : library.books) {
            if (b.getBookId() == bookId) {
                book = b;
                break;
            }
        }
        
        if (book == null) {
            System.out.println(" Book not found!");
            return;
        }
        
        scanner.nextLine(); // Clear the buffer
        
        System.out.print("Enter new title (press Enter to keep current: " + book.getTitle() + "): ");
        String title = scanner.nextLine();
        if (title.isEmpty()) {
            title = book.getTitle();
        }
        
        System.out.print("Enter new author (press Enter to keep current: " + book.getAuthor() + "): ");
        String author = scanner.nextLine();
        if (author.isEmpty()) {
            author = book.getAuthor();
        }
        
        System.out.print("Enter new genre (press Enter to keep current: " + book.getGenre() + "): ");
        String genre = scanner.nextLine();
        if (genre.isEmpty()) {
            genre = book.getGenre();
        }
        
        System.out.print("Enter new number of copies (current: " + book.getAvailableCopies() + "): ");
        String copiesStr = scanner.nextLine();
        int copies = copiesStr.isEmpty() ? book.getAvailableCopies() : Integer.parseInt(copiesStr);
        
        if (bookManager.updateBook(bookId, title, author, genre, copies)) {
            System.out.println(" Book updated successfully!");
        } else {
            System.out.println(" Failed to update book!");
        }
    }

    private void deleteBook() {
        System.out.println("\n----- Delete a Book -----");
        viewAllBooks();
        
        System.out.print("Enter the ID of the book to delete: ");
        int bookId = getIntInput();
        
        if (bookManager.deleteBook(bookId)) {
            System.out.println(" Book deleted successfully!");
        } else {
            System.out.println(" Cannot delete book! It might be borrowed or not found.");
        }
    }

    private void searchBookByTitle() {
        System.out.println("\n----- Search Book by Title -----");
        scanner.nextLine(); // Clear the buffer
        
        System.out.print("Enter the title to search: ");
        String title = scanner.nextLine();
        
        Book book = bookManager.searchBookByTitle(title);
        
        if (book != null) {
            System.out.println("\nFound Book:");
            displayBook(book);
        } else {
            System.out.println(" No book found with that title!");
        }
    }

    private void searchBooksByAuthor() {
        System.out.println("\n----- Search Books by Author -----");
        scanner.nextLine(); // Clear the buffer
        
        System.out.print("Enter the author name to search: ");
        String author = scanner.nextLine();
        
        List<Book> books = bookManager.searchBooksByAuthor(author);
        
        if (!books.isEmpty()) {
            System.out.println("\nFound Books:");
            for (Book book : books) {
                displayBook(book);
            }
        } else {
            System.out.println(" No books found by that author!");
        }
    }
    private void sortBooksByTitle() {
        System.out.println("\n----- Sort Books by Title -----");
        List<Book> sortedBooks = bookManager.getBooksSortedByTitle();

        if (sortedBooks.isEmpty()) {
            System.out.println(" No books in the library!");
            return;
        }

        System.out.println("\nID | Title | Author | Genre | Available Copies");
        System.out.println("------------------------------------------");

        for (Book book : sortedBooks) {
            displayBook(book);
        }
    }
    private void sortBooksByAuthor() {
        System.out.println("\n----- Sort Books by Author -----");
        List<Book> sortedBooks = bookManager.getBooksSortedByAuthor();

        if (sortedBooks.isEmpty()) {
            System.out.println(" No books in the library!");
            return;
        }

        System.out.println("\nID | Title | Author | Genre | Available Copies");
        System.out.println("------------------------------------------");

        for (Book book : sortedBooks) {
            displayBook(book);
        }
    }
    private void sortBooksByGenre() {
        System.out.println("\n----- Sort Books by Genre -----");
        List<Book> sortedBooks = bookManager.getBooksSortedByGenre();

        if (sortedBooks.isEmpty()) {
            System.out.println(" No books in the library!");
            return;
        }

        System.out.println("\nID | Title | Author | Genre | Available Copies");
        System.out.println("------------------------------------------");

        for (Book book : sortedBooks) {
            displayBook(book);
        }
    }

    private void viewAllBooks() {
        System.out.println("\n----- All Books -----");
        List<Book> books = bookManager.getAllBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books in the library!");
            return;
        }
        
        System.out.println("\nID | Title | Author | Genre | Available Copies");
        System.out.println("------------------------------------------");
        
        for (Book book : books) {
            displayBook(book);
        }
    }

    private void displayBook(Book book) {
        System.out.println(book.getBookId() + " | " + book.getTitle() + " | " + 
                          book.getAuthor() + " | " + book.getGenre() + " | " + 
                          book.getAvailableCopies());
    }

    private void showMemberMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== MEMBER MANAGEMENT =====");
            System.out.println("1. Add a new member");
            System.out.println("2. Update a member");
            System.out.println("3. Delete a member");
            System.out.println("4. View member details");
            System.out.println("5. View all members");
            System.out.println("6. Back to main menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    addMember();
                    break;
                case 2:
                    updateMember();
                    break;
                case 3:
                    deleteMember();
                    break;
                case 4:
                    viewMemberDetails();
                    break;
                case 5:
                    viewAllMembers();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addMember() {
        System.out.println("\n----- Add a New Member -----");
        scanner.nextLine(); // Clear the buffer
        
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        
        memberManager.addMember(name, email, phone);
    }

    private void updateMember() {
        System.out.println("\n----- Update a Member -----");
        viewAllMembers();
        
        System.out.print("Enter the ID of the member to update: ");
        int memberId = getIntInput();
        
        Member member = memberManager.getMemberById(memberId);
        
        if (member == null) {
            System.out.println("❌ Member not found!");
            return;
        }
        
        scanner.nextLine(); // Clear the buffer
        
        System.out.print("Enter new name (press Enter to keep current: " + member.getName() + "): ");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = member.getName();
        }
        
        System.out.print("Enter new email (press Enter to keep current: " + member.getEmail() + "): ");
        String email = scanner.nextLine();
        if (email.isEmpty()) {
            email = member.getEmail();
        }
        
        System.out.print("Enter new phone (press Enter to keep current: " + member.getPhone() + "): ");
        String phone = scanner.nextLine();
        if (phone.isEmpty()) {
            phone = member.getPhone();
        }
        
        if (memberManager.updateMember(memberId, name, email, phone)) {
            System.out.println("✅ Member updated successfully!");
        } else {
            System.out.println("❌ Failed to update member!");
        }
    }

    private void deleteMember() {
        System.out.println("\n----- Delete a Member -----");
        viewAllMembers();
        
        System.out.print("Enter the ID of the member to delete: ");
        int memberId = getIntInput();
        
        if (memberManager.deleteMember(memberId)) {
            System.out.println("✅ Member deleted successfully!");
        } else {
            System.out.println("❌ Cannot delete member! They might have borrowed books or not found.");
        }
    }

    private void viewMemberDetails() {
        System.out.println("\n----- View Member Details -----");
        
        System.out.print("Enter the ID of the member: ");
        int memberId = getIntInput();
        
        Member member = memberManager.getMemberById(memberId);
        
        if (member != null) {
            System.out.println("\nMember Details:");
            displayMember(member);
            
            // Also show borrowed books
            List<BorrowingRecord> records = borrowingManager.getBorrowingRecordsForMember(memberId);
            if (!records.isEmpty()) {
                System.out.println("\nBorrowed Books:");
                for (BorrowingRecord record : records) {
                    Book book = null;
                    for (Book b : library.books) {
                        if (b.getBookId() == record.getBookId()) {
                            book = b;
                            break;
                        }
                    }
                    
                    if (book != null) {
                        System.out.println("- " + book.getTitle() + " (Borrowed on: " + record.getBorrowDate() + 
                                         (record.isReturned() ? ", Returned on: " + record.getReturnDate() : ", Not returned yet") + ")");
                    }
                }
            } else {
                System.out.println("No borrowing records for this member.");
            }
            
        } else {
            System.out.println("❌ Member not found!");
        }
    }

    private void viewAllMembers() {
        System.out.println("\n----- All Members -----");
        List<Member> members = memberManager.getAllMembers();
        
        if (members.isEmpty()) {
            System.out.println("No members in the library!");
            return;
        }
        
        System.out.println("\nID | Name | Email | Phone");
        System.out.println("---------------------------");
        
        for (Member member : members) {
            displayMember(member);
        }
    }

    private void displayMember(Member member) {
        System.out.println(member.getMemberId() + " | " + member.getName() + " | " + 
                          member.getEmail() + " | " + member.getPhone());
    }

    private void showBorrowingMenu() {
        boolean back = false;
        
        while (!back) {
            System.out.println("\n===== BORROWING MANAGEMENT =====");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. View all borrowing records");
            System.out.println("4. View active borrowings");
            System.out.println("5. Back to main menu");
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput();
            
            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewAllBorrowingRecords();
                    break;
                case 4:
                    viewActiveBorrowings();
                    break;
                    case 5:
                        back = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    
        private void borrowBook() {
            System.out.println("\n----- Borrow a Book -----");
            
            // Display available books
            System.out.println("\nAvailable Books:");
            System.out.println("ID | Title | Author | Genre | Available Copies");
            System.out.println("------------------------------------------");
            
            for (Book book : library.books) {
                if (book.getAvailableCopies() > 0) {
                    displayBook(book);
                }
            }
            
            System.out.print("\nEnter book ID to borrow: ");
            int bookId = getIntInput();
            
            // Display members
            viewAllMembers();
            
            System.out.print("Enter member ID who is borrowing: ");
            int memberId = getIntInput();
            
            BorrowingRecord record = borrowingManager.borrowBook(bookId, memberId);
            
            if (record != null) {
                System.out.println("✅ Book borrowed successfully!");
                
                // Get book and member details
                Book book = null;
                Member member = null;
                
                for (Book b : library.books) {
                    if (b.getBookId() == bookId) {
                        book = b;
                        break;
                    }
                }
                
                for (Member m : library.members) {
                    if (m.getMemberId() == memberId) {
                        member = m;
                        break;
                    }
                }
                
                if (book != null && member != null) {
                    System.out.println("Book: " + book.getTitle() + " borrowed by: " + member.getName());
                    System.out.println("Borrow Date: " + record.getBorrowDate());
                }
            } else {
                System.out.println("❌ Failed to borrow book! Book might not be available or invalid member/book ID.");
            }
        }
    
        private void returnBook() {
            System.out.println("\n----- Return a Book -----");
            
            // Display active borrowings
            viewActiveBorrowings();
            
            System.out.print("\nEnter book ID to return: ");
            int bookId = getIntInput();
            
            if (borrowingManager.returnBook(bookId)) {
                System.out.println("✅ Book returned successfully!");
                
                // Get book details
                Book book = null;
                for (Book b : library.books) {
                    if (b.getBookId() == bookId) {
                        book = b;
                        break;
                    }
                }
                
                if (book != null) {
                    System.out.println("Book: " + book.getTitle() + " has been returned.");
                }
            } else {
                System.out.println("❌ Failed to return book! Book might not be borrowed or invalid book ID.");
            }
        }
    
        private void viewAllBorrowingRecords() {
            System.out.println("\n----- All Borrowing Records -----");
            List<BorrowingRecord> records = borrowingManager.getAllBorrowingRecords();
            
            if (records.isEmpty()) {
                System.out.println("No borrowing records found!");
                return;
            }
            
            displayBorrowingRecords(records);
        }
    
        private void viewActiveBorrowings() {
            System.out.println("\n----- Active Borrowings -----");
            List<BorrowingRecord> records = borrowingManager.getActiveBorrowingRecords();
            
            if (records.isEmpty()) {
                System.out.println("No active borrowings found!");
                return;
            }
            
            displayBorrowingRecords(records);
        }
    
        private void displayBorrowingRecords(List<BorrowingRecord> records) {
            System.out.println("\nBook ID | Book Title | Member ID | Member Name | Borrow Date | Return Date");
            System.out.println("-------------------------------------------------------------------------");
            
            for (BorrowingRecord record : records) {
                Book book = null;
                Member member = null;
                
                for (Book b : library.books) {
                    if (b.getBookId() == record.getBookId()) {
                        book = b;
                        break;
                    }
                }
                
                for (Member m : library.members) {
                    if (m.getMemberId() == record.getMemberId()) {
                        member = m;
                        break;
                    }
                }
                
                if (book != null && member != null) {
                    System.out.println(record.getBookId() + " | " + book.getTitle() + " | " + 
                                      record.getMemberId() + " | " + member.getName() + " | " + 
                                      record.getBorrowDate() + " | " + 
                                      (record.isReturned() ? record.getReturnDate() : "Not returned yet"));
                }
            }
        }
    
        private int getIntInput() {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine(); // Clear the buffer
                return -1; // Return an invalid value
            }
        }
    
        public static void main(String[] args) {
            LibraryMenu menu = new LibraryMenu();
            menu.start();
        }
    }