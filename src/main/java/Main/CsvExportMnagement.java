package Main;

import Utils.CsvExporter;
import Utils.Logger;
import java.util.Scanner;
import Dao.BookDao;
import Dao.BookDaoImpl;
import Dao.MemberDao;
import Dao.MemberDaoImpl;
import Exceptions.DatabaseException;

public class CsvExportMnagement {
    public static void exportDataMenu(Scanner scanner) { // Pass Scanner instance
        // Create instances of DAO implementations
        BookDao bookDao = new BookDaoImpl();
        MemberDao memberDao = new MemberDaoImpl();

        while (true) {
            System.out.println("\n=== 📤 Export Data ===");
            System.out.println("1. Export Books to CSV");
            System.out.println("2. Export Members to CSV");
            System.out.println("3. 🔙 Back to Main Menu");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) { // Check if input exists
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        try {
                            CsvExporter.exportBooks(bookDao.getAllBooks());
                        } catch (DatabaseException e) {
                            System.err.println("Error exporting books: " + e.getMessage());
                            Logger.log("Failed to export books: " + e.getMessage());
                        }
                        break;

                    case 2:
                        try {
                            CsvExporter.exportMembers(memberDao.getAllMembers());
                        } catch (DatabaseException e) {
                            System.err.println("Error exporting members: " + e.getMessage());
                            Logger.log("Failed to export members: " + e.getMessage());
                        }
                        break; // ✅ Added missing break

                    case 3:
                        return; // ✅ Back to Main Menu

                    default:
                        System.out.println("❌ Invalid choice! Please try again.");
                }
            } else {
                System.out.println("❌ Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
