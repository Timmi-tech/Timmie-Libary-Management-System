package Collection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Models.Book;
import Models.BorrowingRecord;
import Models.Member;
import Utils.Logger;

import java.util.HashMap;

public class Borrowingmanagement {
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();
    private HashMap<Integer, Integer> borrowedBooks = new HashMap<>();
    public Library library;  // Reference to Library

    public Borrowingmanagement(Library library) {
        this.library = library;
    }
    // Borrow a book
    public BorrowingRecord borrowBook(int bookId, int memberId) {
        // Check if book exists and is available
        Book book = getBookById(bookId);
        if (book == null || book.getAvailableCopies() <= 0) {
            return null;
        }
        
        // Check if member exists
        Member member = getMemberById(memberId);
        if (member == null) {
            return null;
        }
        
        // Create borrowing record
        BorrowingRecord record = new BorrowingRecord(bookId, memberId, LocalDate.now());
        borrowingRecords.add(record);
        
        // Update book's available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        
        // Update borrowedBooks map
        borrowedBooks.put(bookId, memberId);
        
        Logger.log("Book borrowed: " + book.getTitle() + " by member: " + member.getName());
        return record;
    }
    

    // Return a book
    public boolean returnBook(int bookId) {
        // Check if book is borrowed
        if (!borrowedBooks.containsKey(bookId)) {
            return false;
        }
        
        // Find the borrowing record
        BorrowingRecord record = null;
        for (BorrowingRecord r : borrowingRecords) {
            if (r.getBookId() == bookId && !r.isReturned()) {
                record = r;
                break;
            }
        }
        
        if (record == null) {
            return false;
        }
        
        // Update the record with return date
        record.setReturnDate(LocalDate.now());
        
        // Update book's available copies
        Book book = getBookById(bookId);
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        
        // Remove from borrowedBooks map
        borrowedBooks.remove(bookId);
        
        Logger.log("Book returned: " + book.getTitle());
        return true;
    }
    
    
     //Get all borrowing records
     
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return new ArrayList<>(borrowingRecords);
    }
    

     // Get active borrowing records (not returned yet)
     
    public List<BorrowingRecord> getActiveBorrowingRecords() {
        List<BorrowingRecord> active = new ArrayList<>();
        for (BorrowingRecord record : borrowingRecords) {
            if (!record.isReturned()) {
                active.add(record);
            }
        }
        return active;
    }
    
    
    // Get borrowing records for a specific member
    
    public List<BorrowingRecord> getBorrowingRecordsForMember(int memberId) {
        List<BorrowingRecord> memberRecords = new ArrayList<>();
        for (BorrowingRecord record : borrowingRecords) {
            if (record.getMemberId() == memberId) {
                memberRecords.add(record);
            }
        }
        return memberRecords;
    } 
     // Helper method to get a book by ID
     
    private Book getBookById(int bookId) {
        for (Book book : library.books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }
      
     // Helper method to get a member by ID
    private Member getMemberById(int memberId) {
        for (Member member : library.members) {
            if (member.getMemberId() == memberId) {
                return member;
            }
        }
        return null;
    }

}
