package Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import Utils.Logger;
import Models.Member;


public class MemberManagement {
    public Library library;  // Reference to Library

    public MemberManagement(Library library) {
        this.library = library;
    }
    
//Add a new member to the library
    public Member addMember(String name, String email, String phone) {
        int memberId = library.nextMemberId++;  // Access nextMemberId from Library
        Member member = new Member(memberId, name, email, phone);
        library.members.add(member);
        Logger.log("Added new member: " + member.getName());
        System.out.println(" Member added successfully!");
        return member;
    }
    
    
     // Update member details
    public boolean updateMember(int memberId, String name, String email, String phone) {
        for (Member member : library.members) {
            if (member.getMemberId() == memberId) {
                member.setName(name);
                member.setEmail(email);
                member.setPhone(phone);
                Logger.log("Updated member: " + member.getName());
                return true;
            }
        }
        return false;
    }
    
    
     // Delete a member from the library
    
    public boolean deleteMember(int memberId) {
        // Check if member has borrowed books
        if (library.borrowedBooks.containsValue(memberId)) {
            return false; // Can't delete a member with borrowed books
        }
        
        for (Iterator<Member> iterator = library.members.iterator(); iterator.hasNext();) {
            Member member = iterator.next();
            if (member.getMemberId() == memberId) {
                iterator.remove();
                Logger.log("Deleted member with ID: " + memberId);
                return true;
            }
        }
        return false;
    }
    
    
     // Get a member by ID
    public Member getMemberById(int memberId) {
        for (Member member : library.members) {
            if (member.getMemberId() == memberId) {
                return member;
            }
        }
        return null;
    }
    
     // Get all members in the library
    public List<Member> getAllMembers() {
        return new ArrayList<>(library.members);
    }
    
}
