package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final String LOG_FILE = "library_log.txt";
    private static BufferedWriter writer = null;
    
    /**
     * Initializes the log file writer
     * @throws IOException if the log file cannot be accessed or created
     */
    private static void initWriter() throws IOException {
        if (writer == null) {
            writer = new BufferedWriter(new FileWriter(LOG_FILE, true));
        }
    }
    
    /**
     * Logs a message to the log file
     * @param message The message to log
     * @return true if logging was successful, false otherwise
     */
    public static boolean log(String message) {
        try {
            // Initialize writer if not already open
            initWriter();
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write("[" + timestamp + "] " + message);
            writer.newLine();
            writer.flush(); // Ensure message is written immediately
            
            return true;
        } catch (IOException e) {
            System.err.println("ERROR: Failed to write to log file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Closes the log file writer
     * Should be called when the application exits
     */
    public static void closeLogger() {
        if (writer != null) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                System.err.println("ERROR: Failed to close log file: " + e.getMessage());
            }
        }
    }
}
