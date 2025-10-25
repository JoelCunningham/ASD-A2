package edu.rmit.emergencyeventservice.DTO;

import java.time.format.DateTimeFormatter;

public class CreateEmergencyResponse {
    private String emergencyId;
    private String type;
    private String location;
    private String status;
    private String createdAt;

    public CreateEmergencyResponse(EmergencyEvent emergency) {
        this.emergencyId = emergency.getEmergencyId();
        this.type = emergency.getType();
        this.location = emergency.getLocation();
        this.status = emergency.getStatus();   
        this.createdAt = emergency.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
