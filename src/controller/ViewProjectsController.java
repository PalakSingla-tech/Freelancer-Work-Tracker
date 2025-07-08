package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static service.AuthService.*;

public class ViewProjectsController implements Initializable{
    @FXML
    private TableView<Projects> projectsTable;
    @FXML private TableColumn<Projects, Integer> projectIdCol;
    @FXML private TableColumn<Projects, String> projectNameCol;
    @FXML private TableColumn<Projects, String> clientNameCol;
    @FXML private TableColumn<Projects, Integer>  hourlyRateCol;
    @FXML private TableColumn<Projects, Date> deadlineCol;
    @FXML private TableColumn<Projects, Integer> totalHoursCol;

    ObservableList<Projects> projectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        projectIdCol.setCellValueFactory(new PropertyValueFactory<>("projectId"));
        projectNameCol.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        hourlyRateCol.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        deadlineCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        totalHoursCol.setCellValueFactory(new PropertyValueFactory<>("totalHours"));
        loadProjectData();
    }

    public void loadProjectData() {
        String query = "SELECT projectId, projectName, clientName, hourlyRate, deadline, totalHours FROM projects";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("projectId");
                String projectName = rs.getString("projectName");
                String clientName = rs.getString("clientName");
                int hourlyRate = rs.getInt("hourlyRate");
                String deadline = rs.getString("deadline");
                int totalHours = rs.getInt("totalHours");

                projectList.add(new Projects(id, projectName, clientName, hourlyRate, deadline, totalHours));
            }
            projectsTable.setItems(projectList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
