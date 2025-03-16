package Services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

import Utils.Logger;
import Dao.BookDao;
import Dao.BookDaoImpl;
import Dao.BorrowingDao;
import Dao.BorrowingDaoImpl;
import Exceptions.DatabaseException;
import Models.Book;
import Models.BorrowingRecord;
import Models.Member;
import Dao.MemberDao;
import Dao.MemberDaoImpl;


public class BorrowingService {
    private static Scanner scanner = new Scanner(System.in);

    // Method to borrow a book from the library
    public static void borrowBook() {
        try {
            // First, show available books
            BookDao bookDao = new BookDaoImpl();
            List<Book> availableBooks = bookDao.getAllBooks().stream()
                    .filter(book -> book.getAvailableCopies() > 0)
                    .toList();
            
            if (availableBooks.isEmpty()) {
                System.out.println(" No books available for borrowing");
                return;
            }
            
            System.out.println("\n===  Available Books for Borrowing ===");
            System.out.printf("%-5s | %-25s | %-20s | %-15s | %-10s\n", 
                    "ID", "TITLE", "AUTHOR", "GENRE", "AVAILABLE");
            System.out.println("-----------------------------------------------------------------------------------------------");
            
            for (Book book : availableBooks) {
                String title = book.getTitle().length() > 22 ? 
                        book.getTitle().substring(0, 19) + "..." : book.getTitle();
                String author = book.getAuthor().length() > 17 ? 
                        book.getAuthor().substring(0, 14) + "..." : book.getAuthor();
                String genre = book.getGenre().length() > 12 ? 
                        book.getGenre().substring(0, 9) + "..." : book.getGenre();
                
                System.out.printf("%-5d | %-25s | %-20s | %-15s | %-10d\n", 
                        book.getBookId(), title, author, genre, book.getAvailableCopies());
            }
            
            System.out.print("\nEnter book ID: ");
            String bookIdInput = scanner.nextLine();
            
            int bookId;
            try {
                bookId = Integer.parseInt(bookIdInput);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input: Book ID must be a number");
                return;
            }
            
            // Verify book exists and is available
            Book book = bookDao.getBookById(bookId);
            if (book == null) {
                System.out.println(" No book found with ID: " + bookId);
                return;
            }
            
            if (book.getAvailableCopies() <= 0) {
                System.out.println(" Sorry, there are no available copies of this book");
                return;
            }
            
            // Show the selected book details
            System.out.println("\n=== Selected Book Details ===");
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("Genre: " + book.getGenre());
            System.out.println("Available Copies: " + book.getAvailableCopies());
            
            // Now show members list
            MemberDao memberDao = new MemberDaoImpl();
            List<Member> members = memberDao.getAllMembers();
            
            if (members.isEmpty()) {
                System.out.println(" No members registered in the system");
                return;
            }
            
            System.out.println("\n=== 👥 Library Members ===");
            System.out.printf("%-5s | %-20s | %-25s | %-15s\n", 
                    "ID", "NAME", "EMAIL", "PHONE");
            System.out.println("--------------------------------------------------------------------------------");
            
            for (Member member : members) {
                String name = member.getName().length() > 17 ? 
                        member.getName().substring(0, 14) + "..." : member.getName();
                String email = member.getEmail().length() > 22 ? 
                        member.getEmail().substring(0, 19) + "..." : member.getEmail();
                String phone = member.getPhone().length() > 12 ? 
                        member.getPhone().substring(0, 9) + "..." : member.getPhone();
                
                System.out.printf("%-5d | %-20s | %-25s | %-15s\n", 
                        member.getMemberId(), name, email, phone);
            }
            
            System.out.print("\nEnter member ID: ");
            String memberIdInput = scanner.nextLine();
            
            int memberId;
            try {
                memberId = Integer.parseInt(memberIdInput);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input: Member ID must be a number");
                return;
            }
            
            // Verify member exists
            Member member = memberDao.getMemberById(memberId);
            if (member == null) {
                System.out.println(" No member found with ID: " + memberId);
                return;
            }
            
            // Show the selected member details
            System.out.println("\n===  Selected Member Details ===");
            System.out.println("Name: " + member.getName());
            System.out.println("Email: " + member.getEmail());
            System.out.println("Phone: " + member.getPhone());
            
            // Confirm borrowing
            System.out.print("\nConfirm book borrowing (y/n): ");
            String confirm = scanner.nextLine().toLowerCase();
            if (!confirm.equals("y") && !confirm.equals("yes")) {
                System.out.println(" Book borrowing cancelled");
                return;
            }
            
            // Process borrowing
            BorrowingDao borrowingDao = new BorrowingDaoImpl();
            borrowingDao.borrowBook(bookId, memberId);
            
            System.out.println(" Book borrowed successfully!");
            System.out.println("Book: " + book.getTitle() + " | Member: " + member.getName());
            Logger.log("Book (ID: " + bookId + ") borrowed by member (ID: " + memberId + ")");
        } catch (DatabaseException e) {
            System.out.println(" Error processing borrowing: " + e.getMessage());
            Logger.log("Error processing borrowing: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error in borrowing process: " + e.getMessage());
        }
    }
    // Method to return a book to the library
    public static void returnBook() {
        try {
            System.out.print("Enter borrowing record ID: ");
            String recordIdInput = scanner.nextLine();
            
            int recordId;
            try {
                recordId = Integer.parseInt(recordIdInput);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input: Record ID must be a number");
                return;
            }
            
            // Verify borrowing record exists and is not already returned
            BorrowingDao borrowingDao = new BorrowingDaoImpl();
            BorrowingRecord record = borrowingDao.getBorrowingRecordById(recordId);
            
            if (record == null) {
                System.out.println(" No borrowing record found with ID: " + recordId);
                return;
            }
            
            if (record.getReturnDate() != null) {
                System.out.println(" This book has already been returned on: " + record.getReturnDate());
                return;
            }
            
            // Process return
            borrowingDao.returnBook(recordId);
            
            // Get book and member details for confirmation message
            BookDao bookDao = new BookDaoImpl();
            MemberDao memberDao = new MemberDaoImpl();
            Book book = bookDao.getBookById(record.getBookId());
            Member member = memberDao.getMemberById(record.getMemberId());
            
            System.out.println(" Book returned successfully!");
            System.out.println("Book: " + (book != null ? book.getTitle() : "ID: " + record.getBookId()));
            System.out.println("Member: " + (member != null ? member.getName() : "ID: " + record.getMemberId()));
            Logger.log("Book return processed for record ID: " + recordId);
        } catch (DatabaseException e) {
            System.out.println(" Error processing return: " + e.getMessage());
            Logger.log("Error processing return: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error in return process: " + e.getMessage());
        }
    }
    // Method to display all borrowing records in the library
    public static void displayAllBorrowingRecords() {
    try {
        BorrowingDao borrowingDao = new BorrowingDaoImpl();
        List<BorrowingRecord> records = borrowingDao.getAllBorrowingRecords();
        
        if (records.isEmpty()) {
            System.out.println("ℹ️ No borrowing records found in the system.");
            return;
        }
        
        System.out.println("\n===== BORROWING RECORDS =====");
        System.out.printf("%-5s | %-8s | %-8s | %-12s | %-12s | %-10s\n", 
                "ID", "BOOK ID", "MEMBER", "BORROW DATE", "RETURN DATE", "STATUS");
        System.out.println("--------------------------------------------------------------");
        
        BookDao bookDao = new BookDaoImpl();
        MemberDao memberDao = new MemberDaoImpl();
        
        for (BorrowingRecord record : records) {
            Book book = null;
            Member member = null;
            
            // Try to get book and member details, but continue if not found
            try {
                book = bookDao.getBookById(record.getBookId());
                member = memberDao.getMemberById(record.getMemberId());
            } catch (DatabaseException e) {
                Logger.log("Error retrieving details for record " + record.getRecordId() + ": " + e.getMessage());
            }
            
            String bookInfo = book != null ? book.getTitle() : "ID: " + record.getBookId();
            String memberInfo = member != null ? member.getName() : "ID: " + record.getMemberId();
            String status = record.getReturnDate() == null ? "BORROWED" : "RETURNED";
            
            System.out.printf("%-5d | %-8s | %-8s | %-12s | %-12s | %-10s\n", 
                    record.getRecordId(), 
                    bookInfo.length() > 8 ? bookInfo.substring(0, 5) + "..." : bookInfo, 
                    memberInfo.length() > 8 ? memberInfo.substring(0, 5) + "..." : memberInfo,
                    record.getBorrowDate(), 
                    record.getReturnDate() != null ? record.getReturnDate() : "-", 
                    status);
        }
        
        System.out.println("Total records: " + records.size());
        Logger.log("Retrieved and displayed " + records.size() + " borrowing records");
    } catch (DatabaseException e) {
        System.out.println(" Error retrieving borrowing records: " + e.getMessage());
        Logger.log("Error retrieving borrowing records: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error retrieving borrowing records: " + e.getMessage());
    }
}
// Method to display borrowing records by ID in the library
public static void displayBorrowingRecordsbyId() {
    try {
        System.out.print("Enter borrowing record ID: ");
        String recordIdInput = scanner.nextLine();
        
        int recordId;
        try {
            recordId = Integer.parseInt(recordIdInput);
        } catch (NumberFormatException e) {
            System.out.println(" Invalid input: Record ID must be a number");
            return;
        }
        
        BorrowingDao borrowingDao = new BorrowingDaoImpl();
        BorrowingRecord record = borrowingDao.getBorrowingRecordById(recordId);
        
        if (record == null) {
            System.out.println(" No borrowing record found with ID: " + recordId);
            return;
        }
        
        // Get additional information to enhance the display
        BookDao bookDao = new BookDaoImpl();
        MemberDao memberDao = new MemberDaoImpl();
        
        Book book = null;
        Member member = null;
        
        try {
            book = bookDao.getBookById(record.getBookId());
        } catch (DatabaseException e) {
            Logger.log("Error retrieving book details: " + e.getMessage());
        }
        
        try {
            member = memberDao.getMemberById(record.getMemberId());
        } catch (DatabaseException e) {
            Logger.log("Error retrieving member details: " + e.getMessage());
        }
        
        // Display record details
        System.out.println("\n===== BORROWING RECORD DETAILS =====");
        System.out.println("Record ID: " + record.getRecordId());
        System.out.println("Book ID: " + record.getBookId());
        System.out.println("Book Title: " + (book != null ? book.getTitle() : "Unknown"));
        System.out.println("Member ID: " + record.getMemberId());
        System.out.println("Member Name: " + (member != null ? member.getName() : "Unknown"));
        System.out.println("Borrow Date: " + record.getBorrowDate());
        System.out.println("Return Date: " + (record.getReturnDate() != null ? record.getReturnDate() : "Not returned yet"));
        System.out.println("Status: " + (record.getReturnDate() == null ? "BORROWED" : "RETURNED"));
        
        // Calculate days borrowed
        LocalDate endDate = record.getReturnDate() != null ? record.getReturnDate() : LocalDate.now();
        long daysBorrowed = ChronoUnit.DAYS.between(record.getBorrowDate(), endDate);
        System.out.println("Days borrowed: " + daysBorrowed);
        
        Logger.log("Retrieved and displayed details for borrowing record ID: " + recordId);
    } catch (DatabaseException e) {
        System.out.println(" Error retrieving borrowing record: " + e.getMessage());
        Logger.log("Error retrieving borrowing record: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error retrieving borrowing record: " + e.getMessage());
    }
}
// method to display all returned records in the library
    public static void displayReturnedRecordss() {
    try {
        BorrowingDao borrowingDao = new BorrowingDaoImpl();
        List<BorrowingRecord> records = borrowingDao.getAllReturnedRecords();
        
        if (records.isEmpty()) {
            System.out.println("ℹ️ No returned records found in the system.");
            return;
        }
        
        System.out.println("\n===== RETURNED RECORDS =====");
        System.out.printf("%-5s | %-8s | %-8s | %-12s | %-12s | %-10s\n", 
                "ID", "BOOK ID", "MEMBER", "BORROW DATE", "RETURN DATE", "STATUS");
        System.out.println("--------------------------------------------------------------");
        
        BookDao bookDao = new BookDaoImpl();
        MemberDao memberDao = new MemberDaoImpl();
        
        for (BorrowingRecord record : records) {
            Book book = null;
            Member member = null;
            
            // Try to get book and member details, but continue if not found
            try {
                book = bookDao.getBookById(record.getBookId());
                member = memberDao.getMemberById(record.getMemberId());
            } catch (DatabaseException e) {
                Logger.log("Error retrieving details for record " + record.getRecordId() + ": " + e.getMessage());
            }
            
            String bookInfo = book != null ? book.getTitle() : "ID: " + record.getBookId();
            String memberInfo = member != null ? member.getName() : "ID: " + record.getMemberId();
            
            System.out.printf("%-5d | %-8s | %-8s | %-12s | %-12s | %-10s\n", 
                    record.getRecordId(), 
                    bookInfo.length() > 8 ? bookInfo.substring(0, 5) + "..." : bookInfo, 
                    memberInfo.length() > 8 ? memberInfo.substring(0, 5) + "..." : memberInfo,
                    record.getBorrowDate(), 
                    record.getReturnDate(), 
                    "RETURNED");
        }
        
        System.out.println("Total records: " + records.size());
        Logger.log("Retrieved and displayed " + records.size() + " returned records");
    } catch (DatabaseException e) {
        System.out.println(" Error retrieving returned records: " + e.getMessage());
        Logger.log("Error retrieving returned records: " + e.getMessage());
    } catch (Exception e) {
        System.out.println(" Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error retrieving returned records: " + e.getMessage());
    }
}
}
