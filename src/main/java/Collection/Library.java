package Collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Book;
import Models.BorrowingRecord;
import Models.Member;

public class Library {
    // Collections to store data
    public List<Book> books;
    public List<Member> members;
    public List<BorrowingRecord> borrowingRecords;
    public Map<Integer, Integer> borrowedBooks; // Key: bookId, Value: memberId
    
    // Counters for IDs
    public int nextBookId = 1;
    public int nextMemberId = 1;
    public int nextRecordId = 1;
    
    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        borrowingRecords = new ArrayList<>();
        borrowedBooks = new HashMap<>();
    }
}
