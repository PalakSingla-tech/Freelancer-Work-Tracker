import java.sql.*;
import java.util.Scanner;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class WorkSession {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/freelancer_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "2830@412Ps";

    // MySQL Connection
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    private final Scanner scanner = new Scanner(System.in);
    private final HashMap<Integer, LocalDateTime> activeSessions = new HashMap<>();

    // Start a session (store in memory)
    public void startSession()
    {
        System.out.print("Enter project ID to start session for : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if(activeSessions.containsKey(id))
        {
            System.out.println("Session already started for this project Id");
            return;
        }

        activeSessions.put(id, LocalDateTime.now());
        System.out.println("Session started for the project : " + id);
    }

    // Stop a session (store in db)
    public void stopSession()
    {
        System.out.println("Enter project ID to stop the session : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if(!activeSessions.containsKey(id))
        {
            System.out.println("No session is started for the project : " + id);
            return;
        }

        LocalDateTime start = activeSessions.get(id);
        LocalDateTime end = LocalDateTime.now();
        long duration = Duration.between(start, end).toMinutes();

        try(Connection con = getConnection())
        {
            if(con == null) return;
            String sql = "INSERT INTO work_sessions(projectId, startTime, end_time, durationMinutes) VALUES(?, ?, ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(sql);
            insertStmt.setInt(1, id);
            insertStmt.setTimestamp(2, Timestamp.valueOf(start));
            insertStmt.setTimestamp(3, Timestamp.valueOf(end));
            insertStmt.setInt(4, (int) duration);

            int rows = insertStmt.executeUpdate();
            if(rows > 0)
            {
                System.out.println("Work session saved successfully! Duration : " + duration + " minutes");
            }

            // update totalHours in database
            String updateSQL = "UPDATE projects SET totalHours = totalHours + ? / 60 WHERE projectId = ?";
            PreparedStatement updateStmt = con.prepareStatement(updateSQL);
            updateStmt.setDouble(1, duration);
            updateStmt.setInt(2, id);
            updateStmt.executeUpdate();

        }catch(SQLException e)
        {
            System.out.println("Error saving work sessions!" + e.getMessage());
        }
        activeSessions.remove(id);
    }

    public void viewSession()
    {
        try(Connection con = getConnection())
        {
            System.out.print("Enter project ID to view work session");
            int id = scanner.nextInt();
            scanner.nextLine();
            if(con == null) return;
            String sql = "SELECT * FROM work_sessions WHERE projectId = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1,id);

            ResultSet rs = stmt.executeQuery();
            System.out.println("\n=== Work Sessions for project ID " + id + "===");
            while(rs.next())
            {
                System.out.println("Session ID : " + rs.getInt("sessionId"));
                System.out.println("Start Time : " + rs.getTimestamp("startTime"));
                System.out.println("End Time : " + rs.getTimestamp("end_time"));
                System.out.println("Duration : " + rs.getInt("durationMinutes"));
                System.out.println("----------------------------\n");
            }

        }catch(SQLException e)
        {
            System.out.println("Error viewing session details" + e.getMessage());
        }

    }
}
