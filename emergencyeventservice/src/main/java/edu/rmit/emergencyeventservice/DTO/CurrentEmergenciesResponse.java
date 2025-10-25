package edu.rmit.emergencyeventservice.DTO;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CurrentEmergenciesResponse {
    private List<CurrentEmergency> activeEmergencies;

    public CurrentEmergenciesResponse(List<CurrentEmergency> activeEmergencies) {
        this.activeEmergencies = activeEmergencies;
    }

    public List<CurrentEmergency> getActiveEmergencies() {
        return activeEmergencies;
    }

    public void setActiveEmergencies(List<CurrentEmergency> activeEmergencies) {
        this.activeEmergencies = activeEmergencies;
    }

    public static class CurrentEmergency {
        private String emergencyId;
        private String type;
        private String location;
        private String status;
        private String reportedAt;

        public CurrentEmergency(EmergencyEvent emergency) {
            this.emergencyId = emergency.getEmergencyId();
            this.type = emergency.getType();
            this.location = emergency.getLocation();
            this.status = emergency.getStatus();
            this.reportedAt = emergency.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
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

        public String getReportedAt() {
            return reportedAt;
        }

        public void setReportedAt(String reportedAt) {
            this.reportedAt = reportedAt;
        }
    }
}

