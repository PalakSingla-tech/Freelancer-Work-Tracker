package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Clients {
    private final SimpleIntegerProperty clientId;
    private String clientName;
    private final SimpleStringProperty email;
    private final SimpleIntegerProperty phone;
    private final SimpleStringProperty company;

    public Clients(int clientId, String clientName, String email, int phone, String company) {
        this.clientId = new SimpleIntegerProperty(clientId);
        this.clientName = clientName;
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleIntegerProperty(phone);
        this.company = new SimpleStringProperty(company);
    }

    public int getClientId() { return clientId.get(); }
    public String getClientName() { return clientName; }
    public String getEmail() { return email.get(); }
    public String getCompany() { return company.get(); }
    public int getPhone() { return phone.get();}

    public void setClientName(String clientName) { this.clientName = clientName;}
}
