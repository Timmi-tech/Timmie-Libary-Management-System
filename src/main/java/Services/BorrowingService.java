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

    public static void borrowBook() {
        try {
            System.out.print("Enter book ID: ");
            String bookIdInput = scanner.nextLine();
            
            int bookId;
            try {
                bookId = Integer.parseInt(bookIdInput);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input: Book ID must be a number");
                return;
            }
            
            System.out.print("Enter member ID: ");
            String memberIdInput = scanner.nextLine();
            
            int memberId;
            try {
                memberId = Integer.parseInt(memberIdInput);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input: Member ID must be a number");
                return;
            }
            
            // Verify book exists and is available
            BookDao bookDao = new BookDaoImpl();
            Book book = bookDao.getBookById(bookId);
            if (book == null) {
                System.out.println("❌ No book found with ID: " + bookId);
                return;
            }
            
            if (book.getAvailableCopies() <= 0) {
                System.out.println("❌ Sorry, there are no available copies of this book");
                return;
            }
            
            // Verify member exists
            MemberDao memberDao = new MemberDaoImpl();
            Member member = memberDao.getMemberById(memberId);
            if (member == null) {
                System.out.println("❌ No member found with ID: " + memberId);
                return;
            }
            
            // Process borrowing
            BorrowingDao borrowingDao = new BorrowingDaoImpl();
            borrowingDao.borrowBook(bookId, memberId);
            
            System.out.println("✅ Book borrowed successfully!");
            System.out.println("Book: " + book.getTitle() + " | Member: " + member.getName());
            Logger.log("Book (ID: " + bookId + ") borrowed by member (ID: " + memberId + ")");
        } catch (DatabaseException e) {
            System.out.println("❌ Error processing borrowing: " + e.getMessage());
            Logger.log("Error processing borrowing: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error in borrowing process: " + e.getMessage());
        }
    }
    
    public static void returnBook() {
        try {
            System.out.print("Enter borrowing record ID: ");
            String recordIdInput = scanner.nextLine();
            
            int recordId;
            try {
                recordId = Integer.parseInt(recordIdInput);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input: Record ID must be a number");
                return;
            }
            
            // Verify borrowing record exists and is not already returned
            BorrowingDao borrowingDao = new BorrowingDaoImpl();
            BorrowingRecord record = borrowingDao.getBorrowingRecordById(recordId);
            
            if (record == null) {
                System.out.println("❌ No borrowing record found with ID: " + recordId);
                return;
            }
            
            if (record.getReturnDate() != null) {
                System.out.println("❌ This book has already been returned on: " + record.getReturnDate());
                return;
            }
            
            // Process return
            borrowingDao.returnBook(recordId);
            
            // Get book and member details for confirmation message
            BookDao bookDao = new BookDaoImpl();
            MemberDao memberDao = new MemberDaoImpl();
            Book book = bookDao.getBookById(record.getBookId());
            Member member = memberDao.getMemberById(record.getMemberId());
            
            System.out.println("✅ Book returned successfully!");
            System.out.println("Book: " + (book != null ? book.getTitle() : "ID: " + record.getBookId()));
            System.out.println("Member: " + (member != null ? member.getName() : "ID: " + record.getMemberId()));
            Logger.log("Book return processed for record ID: " + recordId);
        } catch (DatabaseException e) {
            System.out.println("❌ Error processing return: " + e.getMessage());
            Logger.log("Error processing return: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error in return process: " + e.getMessage());
        }
    }
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
        System.out.println("❌ Error retrieving borrowing records: " + e.getMessage());
        Logger.log("Error retrieving borrowing records: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("❌ Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error retrieving borrowing records: " + e.getMessage());
    }
}

public static void displayBorrowingRecordsbyId() {
    try {
        System.out.print("Enter borrowing record ID: ");
        String recordIdInput = scanner.nextLine();
        
        int recordId;
        try {
            recordId = Integer.parseInt(recordIdInput);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input: Record ID must be a number");
            return;
        }
        
        BorrowingDao borrowingDao = new BorrowingDaoImpl();
        BorrowingRecord record = borrowingDao.getBorrowingRecordById(recordId);
        
        if (record == null) {
            System.out.println("❌ No borrowing record found with ID: " + recordId);
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
        System.out.println("❌ Error retrieving borrowing record: " + e.getMessage());
        Logger.log("Error retrieving borrowing record: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("❌ Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error retrieving borrowing record: " + e.getMessage());
    }
}
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
        System.out.println("❌ Error retrieving returned records: " + e.getMessage());
        Logger.log("Error retrieving returned records: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("❌ Unexpected error: " + e.getMessage());
        Logger.log("Unexpected error retrieving returned records: " + e.getMessage());
    }
}
}
