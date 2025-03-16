package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Config.DatabaseConnection;
import Exceptions.DatabaseException;
import Models.BorrowingRecord;
import Utils.DatabaseUtils;
import Utils.Logger;

public class BorrowingDaoImpl implements BorrowingDao {

   @Override
    public void borrowBook(int bookId, int memberId) throws DatabaseException {
    String borrowQuery = "INSERT INTO borrowing_records (book_id, member_id, borrow_date) VALUES (?, ?, CURRENT_DATE)";
    String updateQuery = "UPDATE books SET available_copies = available_copies - 1 WHERE book_id = ? AND available_copies > 0";
    
    Connection conn = null;
    PreparedStatement borrowStmt = null;
    PreparedStatement updateStmt = null;
    
    try {
        // Get connection and disable auto-commit for transaction
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        
        // First try to update the book's available copies
        updateStmt = conn.prepareStatement(updateQuery);
        updateStmt.setInt(1, bookId);
        int rowsAffected = updateStmt.executeUpdate();
        
        if (rowsAffected > 0) {
            // If book is available, create the borrowing record
            borrowStmt = conn.prepareStatement(borrowQuery);
            borrowStmt.setInt(1, bookId);
            borrowStmt.setInt(2, memberId);
            borrowStmt.executeUpdate();
            
            // Commit the transaction
            conn.commit();
            Logger.log("Book (ID: " + bookId + ") borrowed by member (ID: " + memberId + ")");
        } else {
            // If book is not available, roll back and throw exception
            conn.rollback();
            throw new DatabaseException("Book is not available for borrowing");
        }
    } catch (SQLException e) {
        // Roll back the transaction in case of error
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            Logger.log("Error during transaction rollback: " + ex.getMessage());
        }
        
        Logger.log("Error borrowing book: " + e.getMessage());
        throw new DatabaseException("Failed to borrow book: " + e.getMessage(), e);
    } finally {
        // Reset auto-commit to default state
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.log("Error resetting auto-commit: " + e.getMessage());
        }
        
        // Close resources
        DatabaseUtils.closeResources(updateStmt, null);
        DatabaseUtils.closeResources(borrowStmt, null);
        // Note: We don't close the connection since it's managed by the singleton
    }
}

    @Override
    public void returnBook(int recordId) throws DatabaseException {
    String returnQuery = "UPDATE borrowing_records SET return_date = CURRENT_DATE WHERE record_id = ? AND return_date IS NULL";
    String updateQuery = "UPDATE books SET available_copies = available_copies + 1 WHERE book_id = " +
            "(SELECT book_id FROM borrowing_records WHERE record_id = ?)";
    String checkQuery = "SELECT book_id FROM borrowing_records WHERE record_id = ? AND return_date IS NULL";
    
    Connection conn = null;
    PreparedStatement returnStmt = null;
    PreparedStatement updateStmt = null;
    PreparedStatement checkStmt = null;
    ResultSet rs = null;
    
    try {
        // Get connection and disable auto-commit for transaction
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        
        // First check if the book is actually borrowed and not returned yet
        checkStmt = conn.prepareStatement(checkQuery);
        checkStmt.setInt(1, recordId);
        rs = checkStmt.executeQuery();
        
        if (rs.next()) {
            // First mark the book as returned in borrowing records
            returnStmt = conn.prepareStatement(returnQuery);
            returnStmt.setInt(1, recordId);
            int returnRowsAffected = returnStmt.executeUpdate();
            
            if (returnRowsAffected > 0) {
                // Then update the available copies in books table
                updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, recordId);
                updateStmt.executeUpdate();
                
                // Commit the transaction
                conn.commit();
                Logger.log("Book return processed for record ID: " + recordId);
            } else {
                // If no rows affected, rollback and throw exception
                conn.rollback();
                throw new DatabaseException("Book already returned or record ID invalid");
            }
        } else {
            // If record not found or already returned, throw exception
            conn.rollback();
            throw new DatabaseException("No active borrowing record found with ID: " + recordId);
        }
    } catch (SQLException e) {
        // Roll back the transaction in case of error
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            Logger.log("Error during transaction rollback: " + ex.getMessage());
        }
        
        Logger.log("Error returning book: " + e.getMessage());
        throw new DatabaseException("Failed to process book return: " + e.getMessage(), e);
    } finally {
        // Reset auto-commit to default state
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            Logger.log("Error resetting auto-commit: " + e.getMessage());
        }
        
        // Close resources
        DatabaseUtils.closeResources(checkStmt, rs);
        DatabaseUtils.closeResources(returnStmt, null);
        DatabaseUtils.closeResources(updateStmt, null);
    }
}   
    @Override
    public List<BorrowingRecord> getAllBorrowingRecords() throws DatabaseException {
        String query = "SELECT * FROM borrowing_records";
        List<BorrowingRecord> records = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                records.add(extractBorrowingRecordFromResultSet(rs));
            }
            
            Logger.log("Retrieved " + records.size() + " borrowing records");
            return records;
        } catch (SQLException e) {
            Logger.log("Error retrieving borrowing records: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve borrowing records", e);
        }
    }
    @Override
    public BorrowingRecord getBorrowingRecordById(int recordId) throws DatabaseException {
        String query = "SELECT * FROM borrowing_records WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, recordId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BorrowingRecord record = extractBorrowingRecordFromResultSet(rs);
                    Logger.log("Retrieved borrowing record with ID: " + recordId);
                    return record;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            Logger.log("Error retrieving borrowing record: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve borrowing record with ID: " + recordId, e);
        }
    }

    private BorrowingRecord extractBorrowingRecordFromResultSet(ResultSet rs) throws SQLException {
        int recordId = rs.getInt("record_id");
        int bookId = rs.getInt("book_id");
        int memberId = rs.getInt("member_id");
        LocalDate borrowDate = rs.getObject("borrow_date", LocalDate.class);
        LocalDate returnDate = rs.getObject("return_date", LocalDate.class);
        
        return new BorrowingRecord(recordId, bookId, memberId, borrowDate, returnDate);
    }
    
    @Override
    public List<BorrowingRecord> getAllReturnedRecords() throws DatabaseException {
        String query = "SELECT * FROM borrowing_records WHERE return_date IS NOT NULL";
        List<BorrowingRecord> records = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                records.add(extractBorrowingRecordFromResultSet(rs));
            }
            
            Logger.log("Retrieved " + records.size() + " returned records");
            return records;
        } catch (SQLException e) {
            Logger.log("Error retrieving returned records: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve returned records", e);
        }
    }
      
}
