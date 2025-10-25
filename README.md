# RMIT Smart Emergency Response System - Partial Implementation

A microservices-based emergency response system designed for RMIT University. This system implmenets a single service to handle the creation and retrieval of emergency events using in-memory data storage.

## Service Details

### Emergency Event Service
- **Service Name**: Emergency Event Service
- **Artifact ID**: emergency-event-service
- **Description**: Manages emergency events and coordinates response activities
- **Port**: 8080

## Project Structure
```
├── emergencyeventservice/  
│   ├── src/main/java/edu/rmit/emergencyeventservice/
│   │   ├── DTO/               
│   │   ├── EmergencyEventController.java
│   │   └── EmergencyEventServiceApplication.java
│   ├── src/test/java/edu/rmit/emergencyeventservice/
│   │   └── EmergencyEventControllerTest.java
│   └── pom.xml
└── README.md                  
```

## Technology Stack
- Java 17
- Spring Boot 3.5.5
- Maven
- Spring Web

## Getting Started
1. Navigate to the `emergencyeventservice` directory
2. Run `mvn spring-boot:run` to start the service
3. The service will be available at `http://localhost:8080`
4. The API endpoints will be accessible at:
   - `POST http://localhost:8080/api/emergencies/create`
   - `GET http://localhost:8080/api/emergencies/current`

## API Documentation


### Base URL
```
http://localhost:8080
```

### Create Emergency Event
**POST** `/api/emergencies/create`

Creates a new emergency event manually.

**Request Body:**
```json
{
  "type": "Fire",
  "location": "080.03.020",
  "reportedBy": "e1234567",
  "severity": "High",
  "description": "Fire alarm triggered in laboratory area"
}
```

**Response (201 Created):**
```json
{
  "emergencyId": "E123456",
  "type": "Fire",
  "location": "080.03.020",
  "status": "Active",
  "createdAt": "2025-10-20T11:30:00Z"
}
```

**Response Fields:**
- `emergencyId`: Unique identifier for the emergency (format: E######)
- `type`: Type of emergency (e.g., Fire, Gas Leak, Medical)
- `location`: Location code where emergency occurred
- `status`: Current status of the emergency (Active, Resolved, etc.)
- `createdAt`: ISO 8601 timestamp when emergency was created

**Validation:**
All fields in the request body are required:
- `type`: Must not be null or empty
- `location`: Must not be null or empty
- `reportedBy`: Must not be null or empty
- `severity`: Must not be null or empty
- `description`: Must not be null or empty

### Get Current Emergencies
**GET** `/api/emergencies/current`

Retrieves all currently active emergency events.

**Response (200 OK):**
```json
{
  "activeEmergencies": [
    {
      "emergencyId": "E123456",
      "type": "Fire",
      "location": "080.03.020",
      "status": "Active",
      "reportedAt": "2025-10-25T11:30:00Z"
    },
    {
      "emergencyId": "E123457",
      "type": "Gas Leak",
      "location": "056.01.002",
      "status": "Active",
      "reportedAt": "2025-10-25T11:50:00Z"
    }
  ]
}
```

**Response Fields:**
- `activeEmergencies`: Array of currently active emergency events
- `emergencyId`: Unique identifier for the emergency
- `type`: Type of emergency
- `location`: Location code where emergency occurred
- `status`: Current status of the emergency
- `reportedAt`: ISO 8601 timestamp when emergency was reported

**Note:** This endpoint only returns emergencies with status "Active". The service includes dummy data for demonstration purposes.

### Error Responses

All error responses follow this standardized format:
```json
{
  "error": "Error Type",
  "message": "Detailed error message"
}
```

**400 Bad Request**
Used when the client sends invalid data or misses required parameters.
```json
{
  "error": "Bad Request",
  "message": "Missing required field: type"
}
```

**500 Internal Server Error**
Used for unexpected server issues.
```json
{
  "error": "Internal Server Error",
  "message": "An unexpected error occurred while processing your request."
}
```

Note that other error responses are not implemented as they are not revelent to implemented endpoints or out of scope. Authentication has not been implemented as it is out of scope for this demonstration. 

## Implementation Details

### Data Storage
- Uses in-memory data storage with `List<EmergencyEvent>`
- Data persists during application runtime
- Includes dummy data for testing purposes

### Dummy Data
The service initializes with sample emergency events:
- Fire emergency at location "080.03.020"
- Gas leak emergency at location "056.01.002"

## Development
This project is part of the Systems Architecture and Design course at RMIT University.