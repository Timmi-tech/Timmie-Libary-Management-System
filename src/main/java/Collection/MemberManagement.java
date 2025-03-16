package Collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;
import Utils.Logger;
import Models.Member;
import Models.Book;

public class MemberManagement {
    public Library library;  // Reference to Library

    public MemberManagement(Library library) {
        this.library = library;
    }
    
//Add a new member to the library
    public Member addMember(String name, String email, String phone) {
        // Check if member with the same email already exists
        for (Member member : library.members) {
            if (member.getEmail().equalsIgnoreCase(email)) {
                Logger.log("Member with email " + email + " already exists.");
                return null; // Member with the same email already exists
            }
        }
        int memberId = library.nextMemberId++;  // Access nextMemberId from Library
        Member member = new Member(memberId, name, email, phone);
        library.members.add(member);
        Logger.log("Added new member: " + member.getName());
        System.out.println("✅ Member added successfully!");
        return member;
    }
    
    /**
     * Update member details
     */
    public boolean updateMember(int memberId, String name, String email, String phone) {
        for (Member member : members) {
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
    
    /**
     * Delete a member from the library
     */
    public boolean deleteMember(int memberId) {
        // Check if member has borrowed books
        if (borrowedBooks.containsValue(memberId)) {
            return false; // Can't delete a member with borrowed books
        }
        
        for (Iterator<Member> iterator = members.iterator(); iterator.hasNext();) {
            Member member = iterator.next();
            if (member.getMemberId() == memberId) {
                iterator.remove();
                Logger.log("Deleted member with ID: " + memberId);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get a member by ID
     */
    public Member getMemberById(int memberId) {
        for (Member member : members) {
            if (member.getMemberId() == memberId) {
                return member;
            }
        }
        return null;
    }
    
    /**
     * Get all members in the library
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }
    
}
