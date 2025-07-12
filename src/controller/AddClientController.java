package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import javafx.scene.control.Alert.AlertType;

import static service.AuthService.*;

public class AddClientController {
    @FXML private TextField emailField;
    @FXML private TextField clientNameField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField companyNameField;

    @FXML
    private void handleAddClient() {
        String email = emailField.getText();
        String clientName = clientNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String companyName = companyNameField.getText();

        if (email.isEmpty() || clientName.isEmpty() || companyName.isEmpty() || phoneNumber == null) {
            showErrorPopup("Error", "Please fill all fields.");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO clients (clientName, email, phone, company) VALUES (?, ?, ?, ?)");
            stmt.setString(1, clientName);
            stmt.setString(2, email);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, companyName);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showSuccessPopup("Successful","Client added successfully!");
                clearForm();
            } else {
                showErrorPopup("Error", "Failed to add client.");
            }
        } catch (SQLException e) {
            showErrorPopup("Error", "Database error: " + e.getMessage());
        }
    }

    private void clearForm() {
        emailField.clear();
        clientNameField.clear();
        phoneNumberField.clear();
        companyNameField.clear();
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
