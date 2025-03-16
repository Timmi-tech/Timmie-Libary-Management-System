package Dao;
import java.util.List;

import Exceptions.DatabaseException;
import Models.BorrowingRecord;

public interface BorrowingDao {
    void borrowBook(int bookId, int memberId) throws DatabaseException;
    void returnBook(int recordId) throws DatabaseException;
    List<BorrowingRecord> getAllBorrowingRecords() throws DatabaseException;
    BorrowingRecord getBorrowingRecordById(int recordId) throws DatabaseException;
    List<BorrowingRecord> getAllReturnedRecords() throws DatabaseException;
}


