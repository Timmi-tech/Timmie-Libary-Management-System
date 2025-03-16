package Main;


import java.util.Scanner;
import Services.BorrowingService;

public class BorrowingManagementMenu {
    private static Scanner scanner = new Scanner(System.in);
    /** 🔄 Borrowing Management Menu */
    void borrowingManagementMenu() {
        
        while (true) {
            System.out.println("\n=== 🔄 Borrowing Management ===");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. Display Borrowing Records");
            System.out.println("4. Display Borrowing Records by Id");
            System.out.println("5. Display All Returned Records");
            System.out.println("6. 🔙 Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    BorrowingService.borrowBook();
                    break;
                case 2:
                    BorrowingService.returnBook();
                    break;
                case 3:
                    BorrowingService.displayAllBorrowingRecords();
                    break;
                case 4:
                    BorrowingService.displayBorrowingRecordsbyId();
                    break;
                case 5:
                    BorrowingService.displayReturnedRecordss();
                    break;
                case 6:
                    return; // Go back to Main Menu
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }
}
