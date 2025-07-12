package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import static service.AuthService.*;

public class ViewProjectController implements Initializable{
    @FXML
    private TableView<Projects> projectTable;
    @FXML private TableColumn<Projects, Integer> projectIdCol;
    @FXML private TableColumn<Projects, String> projectNameCol;
    @FXML private TableColumn<Projects, String> clientNameCol;
    @FXML private TableColumn<Projects, Integer>  hourlyRateCol;
    @FXML private TableColumn<Projects, Date> deadlineCol;
    @FXML private TableColumn<Projects, Integer> totalHoursCol;
    @FXML private TableColumn<Projects, Void> actionCol;

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
        addActionButtonsToTable();
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
            projectTable.setItems(projectList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addActionButtonsToTable() {
        actionCol.setCellFactory(col -> new TableCell<Projects, Void>() {
            private final Button deleteBtn = new Button("Delete");
            private final Button editBtn = new Button("Edit");
            private final HBox actionButtons = new HBox(10); // spacing between buttons

            {
                deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                editBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                deleteBtn.setOnAction(event -> {
                    Projects project = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText("Delete Project: " + project.getProjectName());
                    alert.setContentText("Are you sure you want to delete this project?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        deleteProject(project);
                        getTableView().getItems().remove(project);
                    }
                });

                editBtn.setOnAction(event -> {
                    Projects project = getTableView().getItems().get(getIndex());
                    showEditPopup(project);
                });

                actionButtons.getChildren().addAll(editBtn, deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButtons);
                }
            }
        });
    }

    private void showEditPopup(Projects project) {
        TextInputDialog dialog = new TextInputDialog(project.getProjectName());
        dialog.setTitle("Edit Client");
        dialog.setHeaderText("Editing project: " + project.getProjectName());
        dialog.setContentText("Enter new name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            updateProjectNameInDB(project.getProjectId(), newName);
            project.setProjectName(newName);
            projectTable.refresh();
        });
    }

    private void updateProjectNameInDB(int projectId, String projectName) {
        String query = "UPDATE projects SET projectName = ? WHERE projectId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, projectName);
            stmt.setInt(2, projectId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteProject(Projects project) {
        String query = "DELETE FROM projects WHERE projectId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, project.getProjectId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
