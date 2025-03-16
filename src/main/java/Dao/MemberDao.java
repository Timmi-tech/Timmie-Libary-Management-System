package Dao;

import java.util.List;

import Exceptions.DatabaseException;
import Models.Member;

public interface MemberDao {
    void addMember(Member member) throws DatabaseException;
    void updateMember(Member member) throws DatabaseException;
    void deleteMember(int memberId) throws DatabaseException;
    List<Member> getAllMembers() throws DatabaseException;
    Member getMemberById(int memberId) throws DatabaseException;
}