package Main;


import java.util.Scanner;
import Services.MemberService;

public class MemeberManagementMenu {
    private static Scanner scanner = new Scanner(System.in);

    /** 👤 Member Management Menu */
    void memberManagementMenu() {
        while (true) {
            System.out.println("\n=== 👤 Member Management ===");
            System.out.println("1. Add Member");
            System.out.println("2. Update Member");
            System.out.println("3. Display All Members");
            System.out.println("4. Display Members by Id");
            System.out.println("5. Delete Member");
            System.out.println("6. 🔙 Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    MemberService.addMember();
                    break;
                case 2:
                    MemberService.updateMember();
                    break;
                case 3:
                    MemberService.displayMembers();  
                    break;
                case 4:
                    MemberService.displayMemberById();
                    break;
                case 5:
                    MemberService.deleteMember();
                    break;
                case 6:
                    return; // Go back to Main Menu
                default:
                    System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }
}
