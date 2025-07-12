package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import service.AuthService;

import java.io.IOException;
import java.sql.*;

import static service.AuthService.*;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField securityKeyField;
    @FXML private Hyperlink signupLink;



    @FXML
    public void initialize() {
        // Set the event handler for the hyperlink
        signupLink.setOnAction(event -> {
            try {
                // Load the sign-in page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignUp.fxml"));
                Parent root = loader.load();

                // Get the current stage and set the new scene
                javafx.stage.Stage stage = (javafx.stage.Stage) signupLink.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorPopup("Error", "Failed to load sign-up page.");
            }
        });
    }

    @FXML private CheckBox adminCheckBox;
    @FXML private HBox securityBox;

    @FXML
    private void handleAdminCheck() {
        boolean isAdmin = adminCheckBox.isSelected();
        securityBox.setVisible(isAdmin);
        securityBox.setManaged(isAdmin); // So layout adjusts automatically
    }


    private final AuthService authService = new AuthService();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String enteredKey = securityKeyField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password are required.");
            return;
        }

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");

                // ✅ Check security key only for admin
                if ("admin".equalsIgnoreCase(role)) {
                    String storedKey = rs.getString("securityKey");
                    if (!storedKey.equals(enteredKey)) {
                        showAlert("Security Check Failed", "Invalid security key for admin.");
                        return;
                    }
                }

                // Continue with navigation
                if ("admin".equalsIgnoreCase(role)) {
                    loadAdminDashboard();
                } else {
                    loadUserDashboard();
                }

            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            showErrorPopup("Error", "Failed to load Admin Dashboard.");
            e.printStackTrace();
        }
    }

    private void loadUserDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Dashboard");
            stage.show();
        } catch (IOException e) {
            showErrorPopup("Error", "Failed to load User Dashboard.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show an error pop-up
    private void showErrorPopup(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // Optional header
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show a success pop-up
    private void showSuccessPopup(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);  // Optional header
        alert.setContentText(message);
        alert.showAndWait();
    }
}
