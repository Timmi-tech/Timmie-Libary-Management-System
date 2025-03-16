# Timmie Library Management System

A comprehensive Java-based Library Management System designed to manage books, members, and borrowing records. Built using JDBC with the DAO pattern, file handling, and Java Collections Framework.

![Java](https://img.shields.io/badge/Java-17-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![Maven](https://img.shields.io/badge/Maven-3.8.6-C71A36)

## Features

### Core Features
- **Book Management**  
  Add, update, delete, search, and display books. Track available copies.
- **Member Management**  
  Add, update, delete, and display members.
- **Borrowing Management**  
  Borrow/return books with automatic updates to available copies and borrowing records.
- **Logging & Reporting**  
  Log activities (e.g., borrow/return actions) to library_log.txt. Export book/member data to CSV files.

### Technical Implementation
- **JDBC & DAO Pattern**  
  Database operations are abstracted using DAO interfaces (BookDAO, MemberDAO, BorrowingDAO) and PostgreSQL.
- **File Handling**  
  BufferedWriter for logging and CSV exports.
- **Java Collections**  
  ArrayList and HashMap for in-memory data management (e.g., tracking borrowed books).

## Technologies Used
- **Backend**: Java 17
- **Database**: PostgreSQL (JDBC)
- **Tools**: Maven, Git
- **Libraries**: Java Collections

## Installation & Setup

### Prerequisites
- Java 17 JDK
- PostgreSQL
- Maven

### Steps
1. **Clone the Repository**  
   
```bash
   git clone https://github.com/Timmi-tech/Timmie-Library-Management-System.git
   cd Timmie-Library-Management-System
```

### Database Setup
1. Create a database named library_db.
2. Run the following SQL scripts to create tables:
   
```sql
   CREATE DATABASE library_db;
   \c library_db

   CREATE TABLE books (
       book_id SERIAL PRIMARY KEY,
       title VARCHAR(255) NOT NULL,
       author VARCHAR(255) NOT NULL,
       genre VARCHAR(100) NOT NULL,
       available_copies INTEGER NOT NULL
   );

   CREATE TABLE members (
       member_id SERIAL PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       email VARCHAR(255) UNIQUE NOT NULL,
       phone VARCHAR(20) NOT NULL
   );

   CREATE TABLE borrowing_records (
       record_id SERIAL PRIMARY KEY,
       book_id INTEGER NOT NULL,
       member_id INTEGER NOT NULL,
       borrow_date DATE NOT NULL,
       return_date DATE,
       FOREIGN KEY (book_id) REFERENCES books(book_id),
       FOREIGN KEY (member_id) REFERENCES members(member_id)
   );
   -- Create indexes for better performance
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_books_genre ON books(genre);
CREATE INDEX idx_borrowing_book_id ON borrowing_records(book_id);
CREATE INDEX idx_borrowing_member_id ON borrowing_records(member_id);
CREATE INDEX idx_borrowing_return_date ON borrowing_records(return_date);
```

### Configure Database Connection
Update DatabaseConnection.java with your database credentials:
   
```java
   public class DatabaseConnection {
       private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
       private static final String USER = "your_username";
       private static final String PASSWORD = "your_password";
       // ... update with your credentials
   }
```

### Build and Run
Compile and execute the application using your preferred IDE or command line.

1. **To run the application:**
   - Navigate to the `src/main/java/Menu` directory
   - Run `LibraryMenu.java` as the entry point to access the menu-driven interface
   - For collection-based implementation, go to `src/main/java/Collection/Menu` and run `LibraryMenu.java`

## Code Structure
```
src/
├── main/
│   ├── java/
│   │   ├── Collection/ # (Java Collections Framework, i did all the logic there)
│   │   ├── Config/ # Database configuration
│   │   │   └── DatabaseConnection.java
│   │   ├── Dao/ # DAO interfaces and implementations
│   │   │   ├── BookDao.java
│   │   │   ├── BookDaoImpl.java
│   │   │   ├── BorrowingDao.java
│   │   │   ├── BorrowingDaoImpl.java
│   │   │   ├── MemberDao.java
│   │   │   └── MemberDaoImpl.java
│   │   ├── Exceptions/ # Custom exceptions
│   │   │   └── DatabaseException.java
│   │   ├── Menu/ # Menu-driven interfaces and core logic
│   │   │   ├── LibraryMenu.java # Main menu controller and entry point
│   │   │   ├── BookManagement.java # Book operations menu
│   │   │   ├── BorrowingManagement.java # Borrow/return logic
│   │   │   └── MemberManagement.java # Member operations menu
│   │   ├── Models/ # Data models 
│   │   │   ├── Book.java
│   │   │   ├── BorrowingRecord.java
│   │   │   └── Member.java
│   │   ├── Services/ # Business logic layer
│   │   │   ├── BookService.java
│   │   │   ├── BorrowingService.java
│   │   │   └── MemberService.java
│   │   └── Utils/ # Utility classes
│   │       ├── CsvExporter.java # CSV exports
│   │       ├── DatabaseUtils.java # Database helpers
│   │       └── Logger.java # Logging
│   └── resources/ 
├── test/ # Unit tests 
└── target/ # Build outputs
├── books.csv # Exported book data
├── library_log.txt # Activity logs
├── members.csv # Exported member data
└── pom.xml # Maven config
```

## Usage Examples

### Add a Book
   
```java
   Book book = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 5);
   BookDAO bookDAO = new BookDAOImpl();
   bookDAO.addBook(book);
```

### Add a Member
   
```java
   Member member = new Member("John Doe", "john.doe@example.com", "555-123-4567");
   MemberDAO memberDAO = new MemberDAOImpl();
   memberDAO.addMember(member);
```

### Borrow a Book
   
```java
   BorrowingDAO borrowingDAO = new BorrowingDAOImpl();
   borrowingDAO.borrowBook(1, 1); // bookId=1, memberId=1
```

### Return a Book
   
```java
   BorrowingDAO borrowingDAO = new BorrowingDAOImpl();
   borrowingDAO.returnBook(1, 1); // bookId=1, memberId=1
```

### Export Books to CSV
   
```java
   List<Book> books = bookDAO.getAllBooks();
   CSVExporter.exportBooksToCSV(books, "books.csv");
```

### Export Members to CSV
   
```java
   List<Member> members = memberDAO.getAllMembers();
   CSVExporter.exportMembersToCSV(members, "members.csv");
```

### Using the Menu Interface
1. Run `LibraryMenu.java`
2. Follow the on-screen prompts to:
   - Manage books (add, update, delete, search)
   - Manage members (add, update, delete)
   - Handle borrowing/returning operations
   - Generate reports and exports

### Logging & Reports
- **Logs**: Check logs/library_log.txt for system activities.
  
  Example log entry:
  
```
[INFO] Book borrowed: The Great Gatsby by Member John Doe
```

- **CSV Exports**: Generated in the project root as books.csv or members.csv.

## Development Approach

### Problem Solving Methodology
1. **Initial Planning & Structure**:
   - Analyzed library management requirements
   - Designed database schema
   - Created folder structure following MVC principles
   - Defined model classes (Book, Member, BorrowingRecord)

2. **Implementation Strategy**:
   - Created database connectivity with PostgreSQL
   - Implemented DAO pattern for database operations
   - Developed service layer to handle business logic
   - Built menu-driven interface for user interaction
   - Added utility classes for logging and exports

3. **Collection-Based Implementation**:
   - Implemented parallel logic using Java Collections for in-memory operations
   - Created HashMap and ArrayList structures to manage entities

4. **Testing & Refinement**:
   - Tested core functionalities (book/member management, borrowing)
   - Added error handling and validation
   - Implemented logging for system activities
   - Created export utilities for data reporting

## Assessment Criteria Met
✅ **Functionality**: All core requirements implemented.

✅ **Code Quality**: Modular, clean, and documented.

✅ **JDBC & DAO**: Proper separation of database logic.

✅ **File Handling**: Logging and CSV exports work.

✅ **Collections**: Efficient in-memory data management.

✅ **Error Handling**: Graceful exception handling.


https://github.com/Timmi-tech/Timmie-Libary-Management-System