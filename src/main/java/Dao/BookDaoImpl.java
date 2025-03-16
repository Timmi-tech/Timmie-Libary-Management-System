package Dao;

import Models.Book;
import Config.DatabaseConnection;
import Utils.Logger;
import Exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Utils.DatabaseUtils;

public class BookDaoImpl implements BookDao {
    // Centralized SQL queries as constants
    private static final String SQL_INSERT_BOOK = "INSERT INTO books (title, author, genre, available_copies) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_BOOK = "UPDATE books SET title = ?, author = ?, genre = ?, available_copies = ? WHERE book_id = ?";
    private static final String SQL_DELETE_BOOK = "DELETE FROM books WHERE book_id = ?";
    private static final String SQL_SELECT_ALL_BOOKS = "SELECT * FROM books";
    private static final String SQL_SELECT_BOOK_BY_ID = "SELECT * FROM books WHERE book_id = ?";
    private static final String SQL_SEARCH_BOOKS_BASE = "SELECT * FROM books WHERE (title LIKE ? OR author LIKE ? OR genre LIKE ?)";

    @Override
    public void addBook(Book book) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_BOOK, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getAvailableCopies());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    book.setBookId(rs.getInt(1));
                }
                Logger.log("Book added: " + book.getTitle() + " by " + book.getAuthor());
            }
        } catch (SQLException e) {
            Logger.log("Error adding book: " + e.getMessage());
            throw new DatabaseException("Failed to add book: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, rs);
        }
    }

    @Override
    public void updateBook(Book book) throws DatabaseException {
        PreparedStatement stmt = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_BOOK);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getAvailableCopies());
            stmt.setInt(5, book.getBookId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                Logger.log("Book updated: " + book.getTitle());
            } else {
                throw new DatabaseException("Book with id " + book.getBookId() + " not found for update");
            }
        } catch (SQLException e) {
            Logger.log("Error updating book: " + e.getMessage());
            throw new DatabaseException("Failed to update book: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, null);
        }
    }

    @Override
    public void deleteBook(int bookId) throws DatabaseException {
        PreparedStatement stmt = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE_BOOK);
            stmt.setInt(1, bookId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                Logger.log("Book deleted with ID: " + bookId);
            } else {
                throw new DatabaseException("Book with id " + bookId + " not found for deletion");
            }
        } catch (SQLException e) {
            Logger.log("Error deleting book: " + e.getMessage());
            throw new DatabaseException("Failed to delete book: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, null);
        }
    }

    @Override
    public List<Book> getAllBooks() throws DatabaseException {
        List<Book> books = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_ALL_BOOKS);
            
            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("available_copies")
                );
                books.add(book);
            }
            Logger.log("Retrieved " + books.size() + " books");
        } catch (SQLException e) {
            Logger.log("Error retrieving all books: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve books: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, rs);
        }
        return books;
    }

    @Override
    public Book getBookById(int bookId) throws DatabaseException {
        Book book = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_BOOK_BY_ID);
            stmt.setInt(1, bookId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                book = new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("available_copies")
                );
            }
        } catch (SQLException e) {
            Logger.log("Error retrieving book by ID: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve book with ID " + bookId + ": " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, rs);
        }
        return book;
    }

    @Override
    public List<Book> searchBooks(String keyword, boolean onlyAvailable, String sortBy) throws DatabaseException {
        List<Book> searchResults = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            StringBuilder queryBuilder = new StringBuilder(SQL_SEARCH_BOOKS_BASE);
            
            // Add filter for available books if requested
            if (onlyAvailable) {
                queryBuilder.append(" AND available_copies > 0");
            }
            
            // Add appropriate sorting
            switch (sortBy.toLowerCase()) {
                case "title":
                    queryBuilder.append(" ORDER BY title ASC");
                    break;
                case "most_borrowed":
                    queryBuilder.append(" ORDER BY (SELECT COUNT(*) FROM borrowing_records WHERE books.book_id = borrowing_records.book_id) DESC");
                    break;
                case "newest":
                    queryBuilder.append(" ORDER BY book_id DESC");
                    break;
                default:
                    Logger.log("No specific sorting applied for search query: " + keyword);
            }
            
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(queryBuilder.toString());
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            
            Logger.log("DEBUG: Executing search query with keyword: " + keyword);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                try {
                    Book book = new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("available_copies")
                    );
                    searchResults.add(book);
                } catch (SQLException e) {
                    Logger.log("WARNING: Error mapping result to Book object: " + e.getMessage());
                    // Continue with next result instead of failing entire operation
                }
            }
            
            Logger.log("INFO: Search completed. Found " + searchResults.size() + " books matching '" + keyword + "'");
            
        } catch (SQLException e) {
            Logger.log("ERROR: Database error during book search: " + e.getMessage());
            // Handle specific SQL exceptions differently
            if (e.getSQLState() != null) {
                if (e.getSQLState().startsWith("08")) {
                    // Connection error codes typically start with 08
                    throw new DatabaseException("Database connection issue: " + e.getMessage(), e);
                } else if (e.getSQLState().startsWith("42")) {
                    // Syntax error codes typically start with 42
                    throw new DatabaseException("SQL syntax error in search query: " + e.getMessage(), e);
                } else {
                    throw new DatabaseException("Failed to search books: " + e.getMessage(), e);
                }
            } else {
                throw new DatabaseException("Failed to search books: " + e.getMessage(), e);
            }
        } finally {
            DatabaseUtils.closeResources(stmt, rs);
        }
        
        return searchResults;
    }
}