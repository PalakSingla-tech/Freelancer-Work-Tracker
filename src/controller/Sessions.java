package controller;

public class Sessions {
    private String startTime;
    private String endTime;
    private String duration;

    public Sessions(String startTime, String endTime, String duration) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getDuration() { return duration; }
}
