package edu.rmit.emergencyeventservice.DTO;

public class CreateEmergencyRequest {
    private String type;
    private String location;
    private String reportedBy;
    private String severity;
    private String description;

    public CreateEmergencyRequest() {
    }

    public CreateEmergencyRequest(String type, String location, String reportedBy, String severity, String description) {
        this.type = type;
        this.location = location;
        this.reportedBy = reportedBy;
        this.severity = severity;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
