package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Session {
    private final SimpleIntegerProperty projectId;
    private final SimpleIntegerProperty sessionId;
    private final SimpleStringProperty startTime;
    private final SimpleStringProperty endTime;
    private final SimpleIntegerProperty duration;

    public Session(int projectId, int sessionId, String startTime, String endTime, int duration) {
        this.projectId = new SimpleIntegerProperty(projectId);
        this.sessionId = new SimpleIntegerProperty(sessionId);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.duration = new SimpleIntegerProperty(duration);
    }

    public int getProjectId() { return projectId.get(); }
    public int getSessionId() { return sessionId.get(); }
    public String getStartTime() { return startTime.get(); }
    public String getEndTime() { return endTime.get(); }
    public int getDuration() { return duration.get(); }
}
