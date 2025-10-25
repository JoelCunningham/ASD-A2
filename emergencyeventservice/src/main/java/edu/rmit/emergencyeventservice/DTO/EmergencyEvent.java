package edu.rmit.emergencyeventservice.DTO;

import java.time.LocalDateTime;

public class EmergencyEvent {
    private String emergencyId;
    private String type;
    private String severity;
    private String location;
    private String description;
    private String reportedBy;
    private LocalDateTime timestamp;
    private String status;

    public EmergencyEvent(CreateEmergencyRequest request, String emergencyId) {
        this.emergencyId = emergencyId;
        this.type = request.getType();
        this.severity = request.getSeverity();
        this.location = request.getLocation();
        this.description = request.getDescription();
        this.reportedBy = request.getReportedBy();
        this.timestamp = LocalDateTime.now();
        this.status = "Active";
    }

    public EmergencyEvent(String emergencyId, String type, String severity, String location, String description, String reportedBy) {
        this.emergencyId = emergencyId;
        this.type = type;
        this.severity = severity;
        this.location = location;
        this.description = description;
        this.reportedBy = reportedBy;
        this.timestamp = LocalDateTime.now();
        this.status = "Active";
    }

    public String getEmergencyId() {
        return emergencyId;
    }

    public void setEmergencyId(String emergencyId) {
        this.emergencyId = emergencyId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
