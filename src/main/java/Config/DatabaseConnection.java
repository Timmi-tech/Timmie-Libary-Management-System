package Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Utils.Logger;

public class DatabaseConnection {
    // PostgreSQL connection parameters
    private static final String URL = "jdbc:postgresql://localhost:5432/library_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "08098011";

    private static Connection connection = null;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    // Get database connection
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Logger.log("Database connection established successfully");
            }
        } catch (ClassNotFoundException e) {
            Logger.log("PostgreSQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            Logger.log("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    // Close database connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Logger.log("Database connection closed");
            }
        } catch (SQLException e) {
            Logger.log("Error closing database connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}