import java.sql.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Report {
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

    Scanner scanner = new Scanner(System.in);

    public void generateWeeklyReport()
    {
        generateReportForDays("Weekly Report", 7);
    }

    public void generateMonthlyReport()
    {
        generateReportForDays("Monthly Report", 30);
    }

    public void generateReportForDays(String reportTitle, int days)
    {
        try (Connection conn = getConnection()) {
            LocalDateTime fromDate = LocalDateTime.now().minusDays(days);
            Timestamp fromTimestamp = Timestamp.valueOf(fromDate);

            String sql = """
                SELECT
                    p.projectId,
                    p.projectName,
                    p.hourlyRate,
                    SUM(ws.durationMinutes) AS total_minutes
                FROM projects p
                JOIN work_sessions ws ON p.projectId = ws.projectId
                WHERE ws.startTime >= ?
                GROUP BY p.projectId, p.projectName, p.hourlyRate
                ORDER BY total_minutes DESC
               """;
            if(conn == null) return;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, fromTimestamp);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n===== " + reportTitle + " =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                String title = rs.getString("projectName");
                double rate = rs.getDouble("hourlyRate");
                int minutes = rs.getInt("total_minutes");
                double hours = minutes / 60.0;
                double earnings = hours * rate;

                System.out.println("Project: " + title);
                System.out.printf("Worked: %.2f hours\n", hours);
                System.out.printf("Earnings: ₹%.2f\n", earnings);
                System.out.println("---------------------------");
            }

            if (!found) {
                System.out.println("No work sessions found in the last " + days + " days.");
            }

        } catch (SQLException e) {
            System.out.println("Error generating " + reportTitle + ": " + e.getMessage());
        }
    }
}

