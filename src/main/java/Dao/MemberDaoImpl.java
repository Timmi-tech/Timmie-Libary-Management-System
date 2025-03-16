package Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Config.DatabaseConnection;
import Utils.Logger;
import Models.Member;
import Exceptions.DatabaseException;
import Utils.DatabaseUtils;

public class MemberDaoImpl implements MemberDao {
    // Centralized SQL queries as constants
    private static final String SQL_INSERT_MEMBER = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_MEMBER = "UPDATE members SET name = ?, email = ?, phone = ? WHERE member_id = ?";
    private static final String SQL_DELETE_MEMBER = "DELETE FROM members WHERE member_id = ?";
    private static final String SQL_SELECT_ALL_MEMBERS = "SELECT * FROM members";
    private static final String SQL_SELECT_MEMBER_BY_ID = "SELECT * FROM members WHERE member_id = ?";
    
    @Override
    public void addMember(Member member) throws DatabaseException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    member.setMemberId(rs.getInt(1));
                }
                Logger.log("Member added: " + member.getName());
            }
        } catch (SQLException e) {
            Logger.log("Error adding member: " + e.getMessage());
            throw new DatabaseException("Failed to add member: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, rs);
        }
    }

    @Override
    public void updateMember(Member member) throws DatabaseException {
        PreparedStatement stmt = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_MEMBER);
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getPhone());
            stmt.setInt(4, member.getMemberId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                Logger.log("Member updated: " + member.getName());
            } else {
                throw new DatabaseException("Member with id " + member.getMemberId() + " not found for update");
            }
        } catch (SQLException e) {
            Logger.log("Error updating member: " + e.getMessage());
            throw new DatabaseException("Failed to update member: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, null);
        }
    }

    @Override
    public void deleteMember(int memberId) throws DatabaseException {
        PreparedStatement stmt = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SQL_DELETE_MEMBER);
            stmt.setInt(1, memberId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                Logger.log("Member deleted: " + memberId);
            } else {
                throw new DatabaseException("Member with id " + memberId + " not found for deletion");
            }
        } catch (SQLException e) {
            Logger.log("Error deleting member: " + e.getMessage());
            throw new DatabaseException("Failed to delete member: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, null);
        }
    }

    @Override
    public List<Member> getAllMembers() throws DatabaseException {
        List<Member> members = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL_SELECT_ALL_MEMBERS);
            
            while (rs.next()) {
                int memberId = rs.getInt("member_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                members.add(new Member(memberId, name, email, phone));
            }
            Logger.log("Retrieved " + members.size() + " members");
        } catch (SQLException e) {
            Logger.log("Error retrieving members: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve members: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, rs);
        }
        return members;
    }

    @Override
    public Member getMemberById(int memberId) throws DatabaseException {
        Member member = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_MEMBER_BY_ID);
            stmt.setInt(1, memberId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                member = new Member(memberId, name, email, phone);
                Logger.log("Retrieved member: " + name);
            }
        } catch (SQLException e) {
            Logger.log("Error retrieving member: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve member: " + e.getMessage(), e);
        } finally {
            DatabaseUtils.closeResources(stmt, rs);
        }
        return member;
    }
}