package service;

import java.sql.*;
import java.util.Scanner;
import java.util.regex.*;

public class AuthService {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/freelancer_tracker";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "2830@412Ps";

    public static final String ADMIN_SECRET_KEY = "ADMIN123KEY";  // Store securely in real projects
    // MySQL Connection
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    public static boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters long");
            return false;
        }
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            System.out.println("Password must contain at least one uppercase letter");
            return false;
        }
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            System.out.println("Password must contain at least one lowercase letter");
            return false;
        }
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            System.out.println("Password must contain at least one digit");
            return false;
        }
        return true;
    }

    // Registering controller.User
    public boolean registerUser(String username, String password, String role) {
        if (!isPasswordValid(password)) {
            System.out.println("Registration failed due to invalid password");
            return false;
        }

        try (Connection conn = getConnection()) {
            if (conn == null) return false;

            // Check if username already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT username FROM users WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("Username already exists!");
                return false;
            }

            // Insert new user with role
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.setString(3, role.toLowerCase());
            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
            return false;
        }
    }

    // Logging in controller.User
    public String loginUser(String username, String password) {
        try (Connection conn = getConnection()) {
            if (conn == null) return null;

            PreparedStatement stmt = conn.prepareStatement("SELECT userId, password, role FROM users WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Username does not exist!");
                return null;
            }
            String role = rs.getString("role");

            if ("admin".equalsIgnoreCase(role)) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter Admin Secret Key: ");
                String enteredKey = scanner.nextLine();

                if (!enteredKey.equals(ADMIN_SECRET_KEY)) {
                    System.out.println("Invalid Admin Key! Access denied.");
                    return null;
                }
            }

            String storedPassword = rs.getString("password");
            if (storedPassword.equals(password)) {
                return rs.getString("role");
            } else {
                System.out.println("Password is incorrect");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            return null;
        }
    }
}
