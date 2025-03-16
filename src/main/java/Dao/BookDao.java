package Dao;

import Models.Book;
import java.util.List;

import Exceptions.DatabaseException;

public interface BookDao {
    void addBook(Book book) throws DatabaseException;
    void updateBook(Book book) throws DatabaseException;
    void deleteBook(int bookId) throws DatabaseException;
    List<Book> getAllBooks() throws DatabaseException;
    Book getBookById(int bookId) throws DatabaseException;
    List<Book> searchBooks(String keyword, boolean onlyAvailable, String sortBy) throws DatabaseException;
}