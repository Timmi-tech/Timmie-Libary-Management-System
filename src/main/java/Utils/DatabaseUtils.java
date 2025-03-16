package Utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for database operations
 */
public class DatabaseUtils {
    
    /**
     * Quietly closes a ResultSet without throwing exceptions
     * @param rs ResultSet to close
     */
    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                Logger.log("Error closing ResultSet: " + e.getMessage());
            }
        }
    }
    
    /**
     * Quietly closes a Statement without throwing exceptions
     * @param stmt Statement to close
     */
    public static void closeQuietly(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                Logger.log("Error closing Statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Quietly closes a Connection without throwing exceptions
     * For use with non-singleton connection patterns
     * @param conn Connection to close
     */
    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                Logger.log("Error closing Connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Closes multiple database resources in the correct order:
     * ResultSet, then Statement
     * (Connection is managed by DatabaseConnection for singleton pattern)
     * 
     * @param stmt Statement or PreparedStatement to close
     * @param rs ResultSet to close
     */
    public static void closeResources(Statement stmt, ResultSet rs) {
        closeQuietly(rs);
        closeQuietly(stmt);
    }
    
    /**
     * Closes all database resources in the correct order:
     * ResultSet, then Statement, then Connection
     * For use with non-singleton connection patterns
     * 
     * @param conn Connection to close
     * @param stmt Statement or PreparedStatement to close
     * @param rs ResultSet to close
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        closeQuietly(rs);
        closeQuietly(stmt);
        closeQuietly(conn);
    }
}