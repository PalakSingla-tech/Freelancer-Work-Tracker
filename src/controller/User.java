package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleIntegerProperty userId;
    private final SimpleStringProperty username;
    private final SimpleStringProperty role;
    private final SimpleStringProperty status;

    public User(int userId, String username, String role, String status) {
        this.userId = new SimpleIntegerProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.role = new SimpleStringProperty(role);
        this.status = new SimpleStringProperty(status);
    }

    public int getUserId() { return userId.get(); }
    public String getUsername() { return username.get(); }
    public String getRole() { return role.get(); }
    public String getStatus() { return status.get(); }
}
