package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import Models.Book;
import Models.Member;

public class CsvExporter {
    private static final String BOOKS_CSV = "books.csv";
    private static final String MEMBERS_CSV = "members.csv";
    
    // Export Books
    public static void exportBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_CSV))) {
            writer.write("Book ID,Title,Author,Genre,Available Copies");
            writer.newLine();
            for (Book book : books) {
                writer.write(
                    escapeCSV(String.valueOf(book.getBookId())) + "," + 
                    escapeCSV(book.getTitle()) + "," + 
                    escapeCSV(book.getAuthor()) + "," +
                    escapeCSV(book.getGenre()) + "," + 
                    escapeCSV(String.valueOf(book.getAvailableCopies()))
                );
                writer.newLine();
            }
            System.out.println("✅ Books exported successfully to " + BOOKS_CSV);
            Logger.log("Books exported to CSV");
        } catch (IOException e) {
            System.err.println("Error exporting books to CSV: " + e.getMessage());
            Logger.log("Failed to export books: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Export Members
    public static void exportMembers(List<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MEMBERS_CSV))) {
            writer.write("Member ID,Name,Email,Phone");
            writer.newLine();
            for (Member member : members) {
                writer.write(
                    escapeCSV(String.valueOf(member.getMemberId())) + "," + 
                    escapeCSV(member.getName()) + "," + 
                    escapeCSV(member.getEmail()) + "," + 
                    escapeCSV(member.getPhone())
                );
                writer.newLine();
            }
            System.out.println("✅ Members exported successfully to " + MEMBERS_CSV);
            Logger.log("Members exported to CSV");
        } catch (IOException e) {
            System.err.println("Error exporting members to CSV: " + e.getMessage());
            Logger.log("Failed to export members: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Properly escapes a string for CSV format.
     * - Wraps the value in quotes if it contains commas, quotes, or newlines
     * - Escapes any quotes within the value by doubling them
     * 
     * @param value The string to escape
     * @return The escaped string suitable for CSV
     */
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        
        boolean needsQuotes = value.contains(",") || value.contains("\"") || 
                             value.contains("\n") || value.contains("\r");
        
        if (needsQuotes) {
            // Replace any quotes with doubled quotes for escaping
            String escapedValue = value.replace("\"", "\"\"");
            return "\"" + escapedValue + "\"";
        } else {
            return value;
        }
    }
}