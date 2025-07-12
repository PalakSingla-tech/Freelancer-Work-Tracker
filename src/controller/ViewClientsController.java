package controller;

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

public class ViewClientsController implements Initializable{
    @FXML
    private TableView<Clients> clientTable;
    @FXML private TableColumn<Clients, Integer> clientIdCol;
    @FXML private TableColumn<Clients, String> clientNameCol;
    @FXML private TableColumn<Clients, String> emailCol;
    @FXML private TableColumn<Clients, Integer>  phoneNumberCol;
    @FXML private TableColumn<Clients, String> companyNameCol;
    @FXML private TableColumn<Clients, Void> actionCol;

    ObservableList<Clients> clientList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        clientIdCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("company"));
        addActionButtonsToTable();
        loadClientData();
    }

    public void loadClientData() {
        String query = "SELECT clientId, clientName, email, phone, company FROM clients";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("clientId");
                String clientName = rs.getString("clientName");
                String email = rs.getString("email");
                int phone = rs.getInt("phone");
                String company = rs.getString("company");

                clientList.add(new Clients(id, clientName, email, phone, company));
            }
            clientTable.setItems(clientList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addActionButtonsToTable() {
        actionCol.setCellFactory(col -> new TableCell<Clients, Void>() {
            private final Button deleteBtn = new Button("Delete");
            private final Button editBtn = new Button("Edit");
            private final HBox actionButtons = new HBox(10); // spacing between buttons

            {
                deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                editBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                deleteBtn.setOnAction(event -> {
                    Clients client = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText("Delete Client: " + client.getClientName());
                    alert.setContentText("Are you sure you want to delete this client?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        deleteClient(client);
                        getTableView().getItems().remove(client);
                    }
                });

                editBtn.setOnAction(event -> {
                    Clients client = getTableView().getItems().get(getIndex());
                    showEditPopup(client);
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

    private void showEditPopup(Clients client) {
        TextInputDialog dialog = new TextInputDialog(client.getClientName());
        dialog.setTitle("Edit Client");
        dialog.setHeaderText("Editing client: " + client.getClientName());
        dialog.setContentText("Enter new name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            updateClientNameInDB(client.getClientId(), newName);
            client.setClientName(newName);
            clientTable.refresh();
        });
    }

    private void updateClientNameInDB(int clientId, String newName) {
        String query = "UPDATE clients SET clientName = ? WHERE clientId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newName);
            stmt.setInt(2, clientId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteClient(Clients client) {
        String query = "DELETE FROM clients WHERE clientId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, client.getClientId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
