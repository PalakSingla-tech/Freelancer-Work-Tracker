import java.sql.*;
import java.util.Scanner;

public class AdminPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/freelancer_tracker";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "2830@412Ps";

    // MySQL Connection
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    Scanner scanner = new Scanner(System.in);

    public void viewUsers()
    {
        try(Connection con = getConnection())
        {
            if (con == null) return;
            String sql = "SELECT userId, username, role FROM users";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n===== ALL REGISTERED USERS =====");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("userId"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Role: " + rs.getString("role"));
                System.out.println("--------------------------");
            }
        }catch(SQLException e)
        {
            System.out.println("Error getting all users !" + e.getMessage());
        }
    }
    public void deleteUser()
    {
        try(Connection con = getConnection())
        {
            if (con == null) return;
            System.out.print("Enter user ID to delete: ");
            int userId = scanner.nextInt();
            scanner.nextLine();

            String sql = "DELETE FROM users WHERE userId = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, userId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("controller.User deleted successfully!");
            } else {
                System.out.println("No user found with that ID.");
            }
        }catch(SQLException e)
        {
            System.out.println("Error getting all users !" + e.getMessage());
        }
    }

    public void viewProjects()
    {
        try(Connection con = getConnection())
        {
            if (con == null) return;
            String sql = "SELECT * FROM projects";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n===== ALL PROJECTS =====\n");
            while (rs.next()) {
                System.out.println("Project ID: " + rs.getInt("projectId"));
                System.out.println("Project Name: " + rs.getString("projectName"));
                System.out.println("Client Name: " + rs.getString("clientName"));
                System.out.println("Hourly Rate: " + rs.getDouble("hourlyRate"));
                System.out.println("Deadline: " + rs.getDate("deadline"));
                System.out.println("Total Hours: " + rs.getDouble("totalHours"));
                System.out.println("---------------------------\n");
            }
        }catch(SQLException e)
        {
            System.out.println("Error getting all users !" + e.getMessage());
        }
    }

    public void viewWorkSessions()
    {
        try(Connection con = getConnection())
        {
            if (con == null) return;
            String sql = "SELECT * FROM work_sessions";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n===== ALL WORK SESSIONS =====\n");
            while (rs.next()) {
                System.out.println("Session ID: " + rs.getInt("sessionId"));
                System.out.println("Project ID: " + rs.getInt("projectId"));
                System.out.println("Start Time: " + rs.getTimestamp("startTime"));
                System.out.println("End Time: " + rs.getTimestamp("end_time"));
                System.out.println("Duration (hours): " + rs.getDouble("durationMinutes"));
                System.out.println("-----------------------------\n");
            }
        }catch(SQLException e)
        {
            System.out.println("Error getting all users !" + e.getMessage());
        }
    }
}
