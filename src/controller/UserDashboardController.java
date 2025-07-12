package controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class UserDashboardController implements Initializable {
    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;

    @FXML private Label activeClientLabel;

    @FXML private Label logout;

    private final UserService userService = new UserService();

    @FXML private BarChart<String, Number> weeklyChart;
    @FXML private BarChart<String, Number> monthlyChart;

    @FXML private Label ongoingProjectLabel;

    @FXML private Button addProjectBtn;
    @FXML private Button viewProjectsBtn;
    @FXML private Button addClientBtn;
    @FXML private Button viewClientsBtn;
    @FXML private Button sessionBtn;

    private final String DB_URL = "jdbc:mysql://localhost:3306/freelancer_tracker";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "2830@412Ps";

    @Override
    public void initialize(URL location, ResourceBundle rb)
    {
        loadWeeklyChart();
        loadMonthlyChart();

        int activeCount = userService.getActiveClientCount();
        activeClientLabel.setText(String.valueOf(activeCount));

        int ongoingProject = userService.getOngoingProjectCount();
        ongoingProjectLabel.setText(String.valueOf(ongoingProject));

        logout.setOnMouseClicked(event -> {
            try {
                // Load the login/register screen
                Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login/Register");
                stage.setScene(new Scene(root));
                stage.show();

                // Close the current dashboard window
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition t = new TranslateTransition();
            t.setDuration(Duration.seconds(0.4));
            t.setNode(slider);

            t.setToX(0);
            t.play();

            slider.setTranslateX(-176);
            t.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition t = new TranslateTransition();
            t.setDuration(Duration.seconds(0.4));
            t.setNode(slider);

            t.setToX(-176);
            t.play();

            slider.setTranslateX(0);
            t.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        });

        addProjectBtn.setOnMouseClicked(event -> {
            try {
                // Load the login/register screen
                Parent root = FXMLLoader.load(getClass().getResource("/views/AddProject.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Add Project");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        viewProjectsBtn.setOnMouseClicked(event -> {
            try {
                // Load the login/register screen
                Parent root = FXMLLoader.load(getClass().getResource("/views/ViewProject.fxml"));
                Stage stage = new Stage();
                stage.setTitle("View Projects");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addClientBtn.setOnMouseClicked(event -> {
            try {
                // Load the login/register screen
                Parent root = FXMLLoader.load(getClass().getResource("/views/AddClient.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Add Client");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        viewClientsBtn.setOnMouseClicked(event -> {
            try {
                // Load the login/register screen
                Parent root = FXMLLoader.load(getClass().getResource("/views/ViewClients.fxml"));
                Stage stage = new Stage();
                stage.setTitle("View Clients");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        sessionBtn.setOnMouseClicked(event -> {
            try {
                // Load the login/register screen
                Parent root = FXMLLoader.load(getClass().getResource("/views/WorkSession.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Work Session");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadWeeklyChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("This Week");

        String query = """
           SELECT DAYNAME(startTime) AS day,
           SUM(TIMESTAMPDIFF(HOUR, startTime, end_time)) AS totalHours
           FROM work_sessions
           WHERE WEEK(startTime) = WEEK(CURDATE()) AND YEAR(startTime) = YEAR(CURDATE())
           GROUP BY day
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String day = rs.getString("day");
                int hours = rs.getInt("totalHours");
                series.getData().add(new XYChart.Data<>(day, hours));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        weeklyChart.getData().clear();
        weeklyChart.getData().add(series);
    }

    private void loadMonthlyChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("This Year");

        String query = """
            SELECT MONTHNAME(startTime) AS month,
                SUM(TIMESTAMPDIFF(HOUR, startTime, end_time)) AS totalHours
            FROM work_sessions
            WHERE YEAR(startTime) = YEAR(CURDATE())
            GROUP BY month
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String month = rs.getString("month");
                int hours = rs.getInt("totalHours");
                series.getData().add(new XYChart.Data<>(month, hours));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        monthlyChart.getData().clear();
        monthlyChart.getData().add(series);
    }
}

