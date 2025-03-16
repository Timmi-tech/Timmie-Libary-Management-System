package Services;

import Exceptions.DatabaseException;
import Models.Member;
import Utils.Logger;

import java.util.Scanner;

import Dao.MemberDao;
import Dao.MemberDaoImpl;

public class MemberService {
    private static Scanner scanner = new Scanner(System.in);
    private static MemberDao memberDAO = new MemberDaoImpl();
    
    // method to Add a new member to the library
    public static void addMember() {
        try {
            System.out.print("Enter member name: ");
            String name = scanner.nextLine();
            
            if (name.trim().isEmpty()) {
                System.out.println(" Name cannot be empty. Please try again.");
                return;
            }
            
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            
            // Basic email validation
            if (!email.contains("@") || !email.contains(".")) {
                System.out.println(" Invalid email format. Please try again.");
                return;
            }
            
            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();
            
            Member member = new Member(0, name, email, phone);
            memberDAO.addMember(member);
            System.out.println(" Member added successfully!");
            Logger.log("Added Member: " + name);
        } catch (DatabaseException e) {
            System.out.println(" Error adding member: " + e.getMessage());
            Logger.log("Error adding member: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error adding member: " + e.getMessage());
        }
    }
    // method to Display all members available in the library
    public static void displayMembers() {
        try {
            System.out.println("\n👥 Members:");
            memberDAO.getAllMembers().forEach(System.out::println);
        } catch (DatabaseException e) {
            System.out.println(" Error fetching members: " + e.getMessage());
            Logger.log("Error fetching members: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error fetching members: " + e.getMessage());
        }
    }
    // method to Display a member by ID from the library
    public static void displayMemberById() {
        try {
            System.out.print("Enter member ID: ");
            String input = scanner.nextLine();
            
            int memberId;
            try {
                memberId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input: Please enter a valid numeric ID");
                Logger.log("Error: Invalid member ID input - not a number");
                return;
            }
            
            if (memberId <= 0) {
                System.out.println(" Member ID must be a positive number");
                return;
            }
            
            Member member = memberDAO.getMemberById(memberId);
            if (member == null) {
                System.out.println(" No member found with ID: " + memberId);
            } else {
                System.out.println("\n👤 Member Details:");
                System.out.println(member);
            }
        } catch (DatabaseException e) {
            System.out.println(" Error fetching member: " + e.getMessage());
            Logger.log("Error fetching member: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error fetching member: " + e.getMessage());
        }
    }
    // method to Update a member in the library
    public static void updateMember() {
        try {
            System.out.print("Enter member ID to update: ");
            String input = scanner.nextLine();

            int memberId;
            try {
                memberId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input: Please enter a valid numeric ID");
                Logger.log("Error: Invalid member ID input - not a number");
                return;
            }

            if (memberId <= 0) {
                System.out.println(" Member ID must be a positive number");
                return;
            }

            // Verify the member exists before updating
            Member member = memberDAO.getMemberById(memberId);
            if (member == null) {
                System.out.println(" No member found with ID: " + memberId);
                return;
            }

            System.out.print("Enter new name (or press Enter to keep current): ");
            String name = scanner.nextLine();
            if (!name.trim().isEmpty()) {
                member.setName(name);
            }

            System.out.print("Enter new email (or press Enter to keep current): ");
            String email = scanner.nextLine();
            if (!email.trim().isEmpty()) {
                member.setEmail(email);
            }

            System.out.print("Enter new phone number (or press Enter to keep current): ");
            String phone = scanner.nextLine();
            if (!phone.trim().isEmpty()) {
                member.setPhone(phone);
            }

            memberDAO.updateMember(member);
            System.out.println(" Member updated successfully!");
            Logger.log("Updated Member ID: " + memberId);
        } catch (DatabaseException e) {
            System.out.println(" Error updating member: " + e.getMessage());
            Logger.log("Error updating member: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error updating member: " + e.getMessage());
        }
    }
    // method to Delete a member from the library
    public static void deleteMember() {
        try {
            System.out.print("Enter member ID to delete: ");
            String input = scanner.nextLine();
            
            int memberId;
            try {
                memberId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(" Invalid input: Please enter a valid numeric ID");
                Logger.log("Error: Invalid member ID input - not a number");
                return;
            }
            
            if (memberId <= 0) {
                System.out.println(" Member ID must be a positive number");
                return;
            }
            
            // Verify the member exists before deleting
            Member member = memberDAO.getMemberById(memberId);
            if (member == null) {
                System.out.println(" No member found with ID: " + memberId);
                return;
            }
            
            // Confirm deletion
            System.out.print("Are you sure you want to delete " + member.getName() + "? (y/n): ");
            String confirm = scanner.nextLine().toLowerCase();
            if (!confirm.equals("y") && !confirm.equals("yes")) {
                System.out.println("⚠️ Member deletion cancelled");
                return;
            }
            
            memberDAO.deleteMember(memberId);
            System.out.println(" Member deleted successfully!");
            Logger.log("Deleted Member ID: " + memberId);
        } catch (DatabaseException e) {
            System.out.println(" Error deleting member: " + e.getMessage());
            Logger.log("Error deleting member: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error: " + e.getMessage());
            Logger.log("Unexpected error deleting member: " + e.getMessage());
        }
       
}

}