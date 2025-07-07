package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import service.AuthService;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField roleField;
    @FXML private Hyperlink signinLink;

    @FXML
    public void initialize() {
        // Set the event handler for the hyperlink
        signinLink.setOnAction(event -> {
            try {
                // Load the sign-in page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
                Parent root = loader.load();

                // Get the current stage and set the new scene
                javafx.stage.Stage stage = (javafx.stage.Stage) signinLink.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showErrorPopup("Error", "Failed to load sign-in page.");
            }
        });
    }

    private final AuthService authService = new AuthService();

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();

        if (username.isBlank() || password.isBlank() || role.isBlank()) {
            showErrorPopup("Error", "All fields are required");
            return;
        }

        boolean success = authService.registerUser(username, password, role);
        showAlert(success
                        ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                success ? "Registration Successful!" : "Registration Failed",
                success ? "Welcome " + username + ", your account has been created." : "An error occurred during registration."
        );
    }

    // Method to show an error pop-up
    private void showErrorPopup(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // Optional header
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to show a pop-up alert
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header, optional
        alert.setContentText(message);
        alert.showAndWait();  // Block further execution until the user closes the pop-up
    }
}
