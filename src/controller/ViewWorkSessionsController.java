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

public class ViewWorkSessionsController implements Initializable{
    @FXML
    private TableView<Session> sessionsTable;
    @FXML private TableColumn<Session, Integer> sessionIdCol;
    @FXML private TableColumn<Session, Integer> projectIdCol;
    @FXML private TableColumn<Session, String> startTimeCol;
    @FXML private TableColumn<Session, String> endTimeCol;
    @FXML private TableColumn<Session, Integer>  durationCol;

    ObservableList<Session> sessionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        projectIdCol.setCellValueFactory(new PropertyValueFactory<>("projectId"));
        sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        loadSessionData();
    }

    public void loadSessionData() {
        String query = "SELECT projectId, sessionId, startTime, end_time, durationMinutes FROM work_sessions";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("projectId");
                int sessionId = rs.getInt("sessionId");
                String startTime = rs.getString("startTime");
                String end_time = rs.getString("end_time");
                int duration = rs.getInt("durationMinutes");

                sessionList.add(new Session(id, sessionId, startTime, end_time, duration));
            }
            sessionsTable.setItems(sessionList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
