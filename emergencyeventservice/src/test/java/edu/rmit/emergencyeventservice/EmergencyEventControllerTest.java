package edu.rmit.emergencyeventservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.rmit.emergencyeventservice.DTO.CreateEmergencyRequest;

@SpringBootTest
@AutoConfigureWebMvc
public class EmergencyEventControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateEmergency_Success() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest(
                "Fire",
                "080.03.020",
                "e1234567",
                "High",
                "Fire alarm triggered in laboratory area");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.emergencyId").exists())
                .andExpect(jsonPath("$.emergencyId").isString())
                .andExpect(jsonPath("$.type").value("Fire"))
                .andExpect(jsonPath("$.location").value("080.03.020"))
                .andExpect(jsonPath("$.status").value("Active"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    public void testCreateEmergency_WithEmptyRequest() throws Exception {
        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateEmergencyRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void testCreateEmergency_WithMissingFields() throws Exception {
        String incompleteJson = "{\"type\":\"Fire\"}";

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(incompleteJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required field: location"));
    }

    @Test
    public void testGetCurrentEmergencies_Success() throws Exception {
        mockMvc.perform(get("/api/emergencies/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeEmergencies").isArray())
                .andExpect(jsonPath("$.activeEmergencies").isNotEmpty())
                .andExpect(jsonPath("$.activeEmergencies[0].emergencyId").exists())
                .andExpect(jsonPath("$.activeEmergencies[0].type").exists())
                .andExpect(jsonPath("$.activeEmergencies[0].location").exists())
                .andExpect(jsonPath("$.activeEmergencies[0].status").value("Active"))
                .andExpect(jsonPath("$.activeEmergencies[0].reportedAt").exists());
    }

    @Test
    public void testGetCurrentEmergencies_ResponseStructure() throws Exception {
        mockMvc.perform(get("/api/emergencies/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeEmergencies").isArray())
                .andExpect(jsonPath("$.activeEmergencies[0].emergencyId").isString())
                .andExpect(jsonPath("$.activeEmergencies[0].type").isString())
                .andExpect(jsonPath("$.activeEmergencies[0].location").isString())
                .andExpect(jsonPath("$.activeEmergencies[0].status").value("Active"))
                .andExpect(jsonPath("$.activeEmergencies[0].reportedAt").isString());
    }

    @Test
    public void testCreateAndRetrieveEmergency_Integration() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest(
                "Medical Emergency",
                "Building A, Room 101",
                "e9999999",
                "Critical",
                "Student collapsed in classroom");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.emergencyId").exists())
                .andExpect(jsonPath("$.type").value("Medical Emergency"))
                .andExpect(jsonPath("$.status").value("Active"));

        mockMvc.perform(get("/api/emergencies/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeEmergencies").isArray())
                .andExpect(jsonPath("$.activeEmergencies[*].type").value(org.hamcrest.Matchers.hasItem("Medical Emergency")))
                .andExpect(jsonPath("$.activeEmergencies[*].location").value(org.hamcrest.Matchers.hasItem("Building A, Room 101")));
    }

    @Test
    public void testCreateEmergency_WithSpecialCharacters() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest(
                "Chemical Spill",
                "Lab-101 & Storage Room",
                "e1234567@rmit.edu.au",
                "High",
                "Chemical spill with special chars: @#$%^&*()");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("Chemical Spill"))
                .andExpect(jsonPath("$.location").value("Lab-101 & Storage Room"))
                .andExpect(jsonPath("$.status").value("Active"));
    }

    @Test
    public void testCreateEmergency_MissingType() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        request.setLocation("080.03.020");
        request.setReportedBy("e1234567");
        request.setSeverity("High");
        request.setDescription("Fire alarm triggered");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required field: type"));
    }

    @Test
    public void testCreateEmergency_MissingLocation() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        request.setType("Fire");
        request.setReportedBy("e1234567");
        request.setSeverity("High");
        request.setDescription("Fire alarm triggered");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required field: location"));
    }

    @Test
    public void testCreateEmergency_MissingReportedBy() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        request.setType("Fire");
        request.setLocation("080.03.020");
        request.setSeverity("High");
        request.setDescription("Fire alarm triggered");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required field: reportedBy"));
    }

    @Test
    public void testCreateEmergency_MissingSeverity() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        request.setType("Fire");
        request.setLocation("080.03.020");
        request.setReportedBy("e1234567");
        request.setDescription("Fire alarm triggered");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required field: severity"));
    }

    @Test
    public void testCreateEmergency_MissingDescription() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        request.setType("Fire");
        request.setLocation("080.03.020");
        request.setReportedBy("e1234567");
        request.setSeverity("High");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required field: description"));
    }

    @Test
    public void testCreateEmergency_EmptyStringFields() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        request.setType("");
        request.setLocation("080.03.020");
        request.setReportedBy("e1234567");
        request.setSeverity("High");
        request.setDescription("Fire alarm triggered");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required field: type"));
    }

    @Test
    public void testCreateEmergency_WhitespaceOnlyFields() throws Exception {
        CreateEmergencyRequest request = new CreateEmergencyRequest();
        request.setType("   ");
        request.setLocation("080.03.020");
        request.setReportedBy("e1234567");
        request.setSeverity("High");
        request.setDescription("Fire alarm triggered");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/emergencies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Missing required field: type"));
    }
}
