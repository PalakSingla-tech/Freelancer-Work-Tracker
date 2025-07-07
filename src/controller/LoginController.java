package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import service.AuthService;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

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

    private final AuthService authService = new AuthService();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            showErrorPopup("Error", "Please fill in all fields");
            return;
        }

        String role = authService.loginUser(username, password);
        if (role == null) {
            showErrorPopup("Login failed", "Invalid username or password.");
            return;
        }

        showSuccessPopup("Login Successfull", "Welcome " + username + "!");
        try {
            String fxml = role.equalsIgnoreCase("admin") ? "/views/AdminDashboard.fxml" : "/views/UserDashboard.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(role.toUpperCase() + " Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorPopup("Error","Error loading dashboard");
        }
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
