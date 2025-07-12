package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class UserService {
    private static final String DB_URL = // your database url;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = // your db password;

    public int getActiveClientCount() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM clients";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getOngoingProjectCount()
    {
        int count = 0;
        String query = "SELECT COUNT(*) FROM projects WHERE deadline >= CURDATE()";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
