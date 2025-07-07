import java.sql.*;
import java.util.Scanner;

public class Client {
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

    public void addClient()
    {
        try(Connection con = getConnection())
        {
            System.out.println("\n====ADD CLIENT====");
            System.out.print("Enter Client Name : ");
            String clientName = scanner.nextLine();

            System.out.print("Enter Email : ");
            String email = scanner.nextLine();

            System.out.print("Enter Phone Number : ");
            int phoneNo = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter Company Name : ");
            String companyName = scanner.nextLine();

            String sql = "INSERT INTO clients (clientName, email, phone, company) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStmt = con.prepareStatement(sql);
            insertStmt.setString(1, clientName);
            insertStmt.setString(2, email);
            insertStmt.setInt(3, phoneNo);
            insertStmt.setString(4, companyName);

            int rows = insertStmt.executeUpdate();
            if(rows > 0)
            {
                System.out.println("Client added successfully!\n");
            }

        }catch(SQLException e)
        {
            System.out.println("Error adding client!" + e.getMessage());
        }
    }

    public void viewClients()
    {
        try(Connection con = getConnection())
        {
            System.out.println("\n==== CLIENTS LIST ====\n");

            String sql = "SELECT * FROM clients";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next())
            {
                System.out.println("ID : " + rs.getInt("clientId"));
                System.out.println("Client Name : " + rs.getString("clientName"));
                System.out.println("Client Email : " + rs.getString("email"));
                System.out.println("Company : " + rs.getString("company"));
                System.out.println("---------------------\n");
            }
        }catch(SQLException e)
        {
            System.out.println("Error viewing client!" + e.getMessage());
        }
    }
}
