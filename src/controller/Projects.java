package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Projects {
    private final SimpleIntegerProperty projectId;
    private final SimpleStringProperty projectName;
    private final SimpleStringProperty clientName;
    private final SimpleIntegerProperty hourlyRate;
    private final SimpleStringProperty deadline;
    private final SimpleIntegerProperty totalHours;

    public Projects(int projectId, String projectName, String clientName, int hourlyRate, String deadline, int totalHours) {
        this.projectId = new SimpleIntegerProperty(projectId);
        this.projectName = new SimpleStringProperty(projectName);
        this.clientName = new SimpleStringProperty(clientName);
        this.hourlyRate = new SimpleIntegerProperty(hourlyRate);
        this.deadline = new SimpleStringProperty(deadline);
        this.totalHours = new SimpleIntegerProperty(totalHours);
    }

    public int getProjectId() { return projectId.get(); }
    public String getProjectName() { return projectName.get(); }
    public String getClientName() { return clientName.get(); }
    public int getHourlyRate() { return hourlyRate.get(); }
    public String getDeadline() { return deadline.get(); }
    public int getTotalHours() { return totalHours.get(); }
}
