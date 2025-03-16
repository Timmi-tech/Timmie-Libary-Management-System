package Main;

import java.util.Scanner;
import java.util.InputMismatchException;
import Services.BookService;

public class BookManagementMenu {

    private static Scanner scanner = new Scanner(System.in);

    /** 📚 Book Management Menu */
    void bookManagementMenu() {
        while (true) {
            System.out.println("\n=== 📚 Book Management ===");
            System.out.println("1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Display All Books");
            System.out.println("5. Display Book by Id");
            System.out.println("6. 🔍 Search Books");  
            System.out.println("7. 🔙 Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice;
            
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("❌ Please enter a valid number");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        BookService.addBook();
                        break;
                    case 2:
                        BookService.updateBook();
                        break;
                    case 3:
                        BookService.deleteBook();
                        break;
                    case 4:
                        BookService.displayBooks();
                        break;
                    case 5:
                        BookService.displayBookById();
                        break;
                    case 6:
                        BookService.searchBook();  // Added search function call
                        break;
                    case 7:
                        return; // Go back to Main Menu
                    default:
                        System.out.println("❌ Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.out.println("❌ An error occurred: " + e.getMessage());
                System.out.println("Returning to Book Management Menu...");
            }
        }
    }
}