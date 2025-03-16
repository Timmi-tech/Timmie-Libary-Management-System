package Main;

import java.util.Scanner;
import Utils.Logger;

public class LibraryMenu {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===  Library Management System ===");
            System.out.println("1. Book Management");
            System.out.println("2. Member Management");
            System.out.println("3. Borrowing Management");
            System.out.println("4. Export Data");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {  // Check if there's an integer input
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        BookManagementMenu bookMenu = new BookManagementMenu();
                        bookMenu.bookManagementMenu();
                        break;
                    case 2:
                        MemeberManagementMenu memberMenu = new MemeberManagementMenu(); 
                        memberMenu.memberManagementMenu();
                        break;
                    case 3:
                        BorrowingManagementMenu borrowingMenu = new BorrowingManagementMenu();
                        borrowingMenu.borrowingManagementMenu();
                        break;
                    case 4:
                        CsvExportMnagement.exportDataMenu(scanner); 
                        break;
                    case 5:
                        System.out.println(" Exiting Library Management System...");
                        Logger.log("Exiting Library Management System...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println(" Invalid choice! Please try again.");
                        Logger.log("Invalid choice! Please try again.");
                }
            } else {
                System.out.println(" Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}
