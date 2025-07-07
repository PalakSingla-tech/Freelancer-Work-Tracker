import service.AuthService;

import java.sql.*;
import java.util.Scanner;

public class Project {
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

    // Adding Project
    public boolean addProject()
    {
        try(Connection con = getConnection())
        {
            // Checking connection
            if(con == null) return false;

            System.out.println("\n====== ADD PROJECT ======");
            System.out.print("Enter Project Name : ");
            String projectName = scanner.nextLine();

            System.out.print("Enter Client Name : ");
            String clientName = scanner.nextLine();

            System.out.print("Enter Hourly Rate : ");
            double hourlyRate = scanner.nextDouble();
            scanner.nextLine(); // consume next line

            System.out.print("Enter Deadline (YYYY-MM-DD): ");
            String deadline = scanner.nextLine();

            // Insert new project
            PreparedStatement insertStmt = con.prepareStatement("INSERT INTO projects (projectName, clientName, " +
                    "hourlyRate, deadline) VALUES (?, ?, ?, ?)");
            insertStmt.setString(1, projectName);
            insertStmt.setString(2, clientName);
            insertStmt.setDouble(3, hourlyRate);
            insertStmt.setDate(4, Date.valueOf(deadline));
            int rows = insertStmt.executeUpdate();

            if(rows > 0) {
                System.out.println("Project added successfully!");
                return true;
            }
            else {
                System.out.println("Cannot add project in database");
                return false;
            }
        }catch(SQLException e)
        {
            System.out.println("Error during addition of project. " + e.getMessage());
            return false;
        }
    }

    // Viewing Projects
    public void viewProjects()
    {
        try(Connection con = getConnection())
        {
             String sql = "SELECT *  FROM projects";
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql);

             System.out.println("\n====== PROJECT LIST ======");
             while(rs.next())
             {
                 System.out.println("ID : " + rs.getInt("projectId"));
                 System.out.println("Project Name : " + rs.getString("projectName"));
                 System.out.println("Client Name : " + rs.getString("clientName"));
                 System.out.println("Hourly Rate : " + rs.getDouble("hourlyRate"));
                 System.out.println("Deadline : " + rs.getDate("deadline"));
                 System.out.println("Total Hours : " + rs.getDouble("totalHours"));
                 System.out.println("-----------------------------------");
             }
        }catch(SQLException e)
        {
            System.out.println("Error viewing project list!" + e.getMessage());
        }
    }

    // Deleting Project
    public boolean deleteProject()
    {
        try(Connection con = getConnection())
        {
            // Checking connection
            if(con == null) return false;

            System.out.print("Enter Project ID to delete : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            String sql = "DELETE FROM projects WHERE projectId = ?";
            PreparedStatement deleteStmt = con.prepareStatement(sql);
            deleteStmt.setInt(1, id);

            int rows = deleteStmt.executeUpdate();
            if(rows > 0)
            {
                System.out.println("Project deleted successfully!");
                return true;
            }
            else {
                System.out.println("No project found with that ID!");
                return false;
            }
        }catch(SQLException e)
        {
            System.out.println("Error deleting project!" + e.getMessage());
            return false;
        }
    }

    public boolean editProject()
    {
        try(Connection con = getConnection())
        {
            System.out.print("Enter project ID to update : ");
            int id = scanner.nextInt();

            System.out.print("Enter New Name : ");
            String projectName = scanner.nextLine();

            System.out.println("Enter New Client Name : ");
            String clientName = scanner.nextLine();

            System.out.print("Enter New Hourly Rate : ");
            double hourlyRate = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Enter New Deadline(YYYY-MM-DD) : ");
            String deadline = scanner.nextLine();

            String sql = "UPDATE projects SET projectName = ?, clientName = ?, hourlyRate = ?, deadline = ?";
            PreparedStatement updateStmt = con.prepareStatement(sql);
            updateStmt.setString(1, projectName);
            updateStmt.setString(2, clientName);
            updateStmt.setDouble(3, hourlyRate);
            updateStmt.setDate(4, Date.valueOf(deadline));
            updateStmt.setInt(5, id);

            int rows = updateStmt.executeUpdate();
            if(rows > 0)
            {
                System.out.println("Project edited successfully!");
                return true;
            }
            else {
                System.out.println("No project found with that ID");
                return false;
            }

        }catch(SQLException e)
        {
            System.out.println("Error editing project!" + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args)
    {
        Project mainApp = new Project();
        mainApp.showLoginMenu();
    }

    public void showLoginMenu()
    {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        while (true) {
            System.out.println("\nChoose an option: \n 1. Register \n 2. Login \n 3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 3) {
                System.out.println("GOODBYE !!");
                break;
            }

            System.out.print("Enter a username: ");
            String username = scanner.nextLine();

            System.out.print("Enter a password: ");
            String password = scanner.nextLine();

            if (choice == 1) {
                System.out.print("Enter role (user/admin): ");
                String role = scanner.nextLine().toLowerCase();
                authService.registerUser(username, password, role);
            } else if (choice == 2) {
                String role = authService.loginUser(username, password);
                if(role!=null)
                {
                    if(role.equals("admin"))
                    {
                        showAdminMenu();
                    }
                    else
                    {
                        showUserMenu();
                    }
                }
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    public void showUserMenu()
    {
        Scanner scanner = new Scanner(System.in);
        Project projectManager = new Project();
        WorkSession session = new WorkSession();
        Client client = new Client();
        Report reports = new Report();
        while(true)
        {
            System.out.println("====== Freelancer Work Tracker ======\n");
            System.out.println("1. Add New Project\n");
            System.out.println("2. View Projects\n");
            System.out.println("3. Edit Project\n");
            System.out.println("4. Delete Project\n");
            System.out.println("5. Start Work Session\n");
            System.out.println("6. Stop Work Session\n");
            System.out.println("7, View Work Session\n");
            System.out.println("8. Add Client Info\n");
            System.out.println("9. View Client Info\n");
            System.out.println("10. Generate Weekly Reports\n");
            System.out.println("11. Generate Monthly Reports\n");
            System.out.println("12. Logout\n");

            System.out.print("Enter your choice : ");
            int choice = scanner.nextInt();
            switch(choice)
            {
                case 1:
                    projectManager.addProject();
                    break;

                case 2:
                    projectManager.viewProjects();
                    break;

                case 3:
                    projectManager.editProject();
                    break;

                case 4:
                    projectManager.deleteProject();
                    break;

                case 5:
                    session.startSession();
                    break;

                case 6:
                    session.stopSession();
                    break;

                case 7:
                    session.viewSession();
                    break;

                case 8:
                    client.addClient();
                    break;

                case 9:
                    client.viewClients();
                    break;

                case 10:
                    reports.generateWeeklyReport();
                    break;

                case 11:
                    reports.generateMonthlyReport();
                    break;

                case 12:
                    System.out.println("Logging out user....");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Please enter a valid choice!");
            }
        }
    }

    public void showAdminMenu()
    {
        Scanner scanner = new Scanner(System.in);
        AdminPanel admin = new AdminPanel();
        while(true)
        {
            System.out.println("\n======== WELCOME TO THE ADMIN PANEL ========\n");
            System.out.println("1. View all Users");
            System.out.println("2. Delete a User");
            System.out.println("3. View all Projects User-Wise");
            System.out.println("4. View all Work Sessions");
            System.out.println("5. Logout");

            System.out.print("Enter your choice : ");
            int choice = scanner.nextInt();
            switch(choice)
            {
                case 1:
                    admin.viewUsers();
                    break;

                case 2:
                    admin.deleteUser();
                    break;

                case 3:
                    admin.viewProjects();
                    break;

                case 4:
                    admin.viewWorkSessions();
                    break;

                case 5:
                    System.out.println("Logging out admin!!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice");
            }
        }

    }
}
