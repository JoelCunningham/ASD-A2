package edu.rmit.emergencyeventservice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.rmit.emergencyeventservice.DTO.CreateEmergencyRequest;
import edu.rmit.emergencyeventservice.DTO.CreateEmergencyResponse;
import edu.rmit.emergencyeventservice.DTO.CurrentEmergenciesResponse;
import edu.rmit.emergencyeventservice.DTO.CurrentEmergenciesResponse.CurrentEmergency;
import edu.rmit.emergencyeventservice.DTO.EmergencyEvent;
import edu.rmit.emergencyeventservice.DTO.ErrorResponse;

@RestController
@RequestMapping("/api/emergencies")
public class EmergencyEventController {

    private final List<EmergencyEvent> emergencies = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public EmergencyEventController() {
        List<EmergencyEvent> dummyEmergencies = generateDummyData();
        emergencies.addAll(dummyEmergencies);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEmergency(@RequestBody CreateEmergencyRequest request) {
        try {
            // Validate required fields
            if (request.getType() == null || request.getType().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Missing required field: type");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Missing required field: location");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (request.getReportedBy() == null || request.getReportedBy().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Missing required field: reportedBy");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (request.getSeverity() == null || request.getSeverity().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Missing required field: severity");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
                ErrorResponse error = new ErrorResponse("Bad Request", "Missing required field: description");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            String emergencyId = generateEmergencyId();
            EmergencyEvent emergency = new EmergencyEvent(request, emergencyId);
            emergencies.add(emergency);

            CreateEmergencyResponse response = new CreateEmergencyResponse(emergency);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", "An unexpected error occurred while processing your request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentEmergencies() {
        try {
            List<CurrentEmergency> activeEmergencies = new ArrayList<>();

            for (EmergencyEvent emergency : emergencies) {
                if ("Active".equals(emergency.getStatus())) {
                    activeEmergencies.add(new CurrentEmergency(emergency));
                }
            }

            CurrentEmergenciesResponse response = new CurrentEmergenciesResponse(activeEmergencies);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal Server Error", "An unexpected error occurred while processing your request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private String generateEmergencyId() {
        return "E" + String.format("%06d", counter.incrementAndGet());
    }

    private List<EmergencyEvent> generateDummyData() {
        List<EmergencyEvent> dummyEmergencies = new ArrayList<>();

        EmergencyEvent dummyEmergency1 = new EmergencyEvent(
                generateEmergencyId(),
                "Fire",
                "High",
                "080.03.020",
                "Fire alarm triggered in laboratory area",
                "e1234567");
        dummyEmergency1.setTimestamp(LocalDateTime.now().minusHours(2));
        dummyEmergencies.add(dummyEmergency1);

        EmergencyEvent dummyEmergency2 = new EmergencyEvent(
                generateEmergencyId(),
                "Gas Leak",
                "Medium",
                "056.01.002",
                "Gas leak detected in basement area",
                "e1234568");
        dummyEmergency2.setTimestamp(LocalDateTime.now().minusHours(1));
        dummyEmergencies.add(dummyEmergency2);

        return dummyEmergencies;
    }
}
