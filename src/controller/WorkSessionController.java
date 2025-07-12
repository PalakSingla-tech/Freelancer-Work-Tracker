package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.net.URL;

import static service.AuthService.*;

public class WorkSessionController implements Initializable {

    @FXML private Label timerLabel;
    @FXML private TableView<Sessions> sessionTable;
    @FXML private TableColumn<Sessions, String> startCol;
    @FXML private TableColumn<Sessions, String> endCol;
    @FXML private TableColumn<Sessions, String> durationCol;
    @FXML private Button startButton;
    @FXML private Button stopButton;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Timeline timer;
    private int secondsElapsed = 0;

    private final ObservableList<Sessions> sessionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

        sessionTable.setItems(sessionList);
        stopButton.setDisable(true);

        loadSessionsFromDB();
    }

    public void handleStart() {
        startTime = LocalDateTime.now();
        secondsElapsed = 0;
        startTimer();
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    public void handleStop() {
        endTime = LocalDateTime.now();
        stopTimer();
        startButton.setDisable(false);
        stopButton.setDisable(true);
    }

    public void handleSaveSession() {
        if (startTime == null || endTime == null) {
            showAlert("Please start and stop the session first.");
            return;
        }

        java.time.Duration duration = java.time.Duration.between(startTime, endTime);

        Sessions session = new Sessions(
                startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                formatDuration(duration)
        );

        saveSessionToDB(session);
        sessionList.add(session);
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            timerLabel.setText(formatDuration(secondsElapsed));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) timer.stop();
    }

    private String formatDuration(java.time.Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String formatDuration(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveSessionToDB(Sessions session) {
        String sql = "INSERT INTO work_sessions (startTime, end_time, durationMinutes) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, session.getStartTime());
            stmt.setString(2, session.getEndTime());
            stmt.setString(3, session.getDuration());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSessionsFromDB() {
        String query = "SELECT * FROM work_sessions ORDER BY sessionId DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sessionList.add(new Sessions(
                        rs.getString("startTime"),
                        rs.getString("end_time"),
                        rs.getString("durationMinutes")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
