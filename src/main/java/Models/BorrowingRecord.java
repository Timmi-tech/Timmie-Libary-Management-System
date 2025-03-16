package Models;

import java.time.LocalDate;

public class BorrowingRecord {
    private int recordId;
    private int bookId;
    private int memberId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    // Constructor
    public BorrowingRecord(int recordId, int bookId, int memberId, LocalDate borrowDate, LocalDate returnDate) {
        this.recordId = recordId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Constructor for new borrowing records
    public BorrowingRecord(int bookId, int memberId, LocalDate borrowDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.returnDate = null; // Not returned yet
    }

    // Getters and Setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    public boolean isReturned() {
        return returnDate != null;
    }
    @Override
    public String toString() {
    return String.format(
        " Borrowing Record:\n" +
        "---------------------------------\n" +
        " Record ID  : %d\n" +
        " Book ID    : %d\n" +
        " Member ID  : %d\n" +
        " Borrowed On: %s\n" +
        " Return By  : %s\n" +
        "---------------------------------",
        recordId, bookId, memberId, borrowDate, returnDate != null ? returnDate : "Not Returned"
    );
}

}
