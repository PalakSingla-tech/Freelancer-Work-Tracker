package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import javafx.scene.control.Alert.AlertType;

import static service.AuthService.*;

public class AddProjectController {
    @FXML private TextField projectNameField;
    @FXML private TextField clientNameField;
    @FXML private TextField hourlyRateField;
    @FXML private DatePicker deadlinePicker;

    @FXML
    private void handleAddProject() {
        String projectName = projectNameField.getText();
        String clientName = clientNameField.getText();
        String hourlyRateText = hourlyRateField.getText();
        LocalDate deadline = deadlinePicker.getValue();

        if (projectName.isEmpty() || clientName.isEmpty() || hourlyRateText.isEmpty() || deadline == null) {
            showErrorPopup("Error", "Please fill all fields.");
            return;
        }

        try {
            double hourlyRate = Double.parseDouble(hourlyRateText);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO projects (projectName, clientName, hourlyRate, deadline) VALUES (?, ?, ?, ?)");
            stmt.setString(1, projectName);
            stmt.setString(2, clientName);
            stmt.setDouble(3, hourlyRate);
            stmt.setDate(4, Date.valueOf(deadline));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                showSuccessPopup("Successful","Project added successfully!");
                clearForm();
            } else {
                showErrorPopup("Error", "Failed to add project.");
            }

        } catch (NumberFormatException e) {
            showErrorPopup("Error", "Hourly rate must be a number.");
        } catch (SQLException e) {
            showErrorPopup("Error", "Database error: " + e.getMessage());
        }
    }

    private void clearForm() {
        projectNameField.clear();
        clientNameField.clear();
        hourlyRateField.clear();
        deadlinePicker.setValue(null);
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
