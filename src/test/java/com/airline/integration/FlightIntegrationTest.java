package com.airline.integration;

import com.airline.model.Flight;
import com.airline.repository.FlightRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Flight Integration Tests")
class FlightIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create new flight via POST endpoint")
    void testCreateFlight() throws Exception {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightName("AI-101");
        flight.setSource("Delhi");
        flight.setDestination("Mumbai");
        flight.setTicketPrice(5000.0);

        // Act & Assert
        mockMvc.perform(post("/flight/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightName").value("AI-101"))
                .andExpect(jsonPath("$.source").value("Delhi"))
                .andExpect(jsonPath("$.destination").value("Mumbai"))
                .andExpect(jsonPath("$.ticketPrice").value(5000.0));
    }

    @Test
    @DisplayName("Should get all flights via GET endpoint")
    void testGetAllFlights() throws Exception {
        // Arrange
        Flight flight1 = new Flight();
        flight1.setFlightName("AI-101");
        flight1.setSource("Delhi");
        flight1.setDestination("Mumbai");
        flight1.setTicketPrice(5000.0);
        flightRepository.save(flight1);

        Flight flight2 = new Flight();
        flight2.setFlightName("AI-102");
        flight2.setSource("Mumbai");
        flight2.setDestination("Bangalore");
        flight2.setTicketPrice(4500.0);
        flightRepository.save(flight2);

        // Act & Assert
        mockMvc.perform(get("/flight/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].flightName").value("AI-101"))
                .andExpect(jsonPath("$[1].flightName").value("AI-102"));
    }

    @Test
    @DisplayName("Should get flight by ID via GET endpoint")
    void testGetFlightById() throws Exception {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightName("AI-101");
        flight.setSource("Delhi");
        flight.setDestination("Mumbai");
        flight.setTicketPrice(5000.0);
        Flight savedFlight = flightRepository.save(flight);

        // Act & Assert
        mockMvc.perform(get("/flight/" + savedFlight.getFlightId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightId").value(savedFlight.getFlightId()))
                .andExpect(jsonPath("$.flightName").value("AI-101"))
                .andExpect(jsonPath("$.source").value("Delhi"))
                .andExpect(jsonPath("$.destination").value("Mumbai"))
                .andExpect(jsonPath("$.ticketPrice").value(5000.0));
    }

    @Test
    @DisplayName("Should return error when flight not found by ID")
    void testGetFlightById_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/flight/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Should update flight via PUT endpoint")
    void testUpdateFlight() throws Exception {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightName("AI-101");
        flight.setSource("Delhi");
        flight.setDestination("Mumbai");
        flight.setTicketPrice(5000.0);
        Flight savedFlight = flightRepository.save(flight);

        Flight updatedFlight = new Flight();
        updatedFlight.setFlightName("AI-101-Updated");
        updatedFlight.setSource("Delhi");
        updatedFlight.setDestination("Chennai");
        updatedFlight.setTicketPrice(6000.0);

        // Act & Assert
        mockMvc.perform(put("/flight/" + savedFlight.getFlightId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedFlight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightId").value(savedFlight.getFlightId()))
                .andExpect(jsonPath("$.flightName").value("AI-101-Updated"))
                .andExpect(jsonPath("$.destination").value("Chennai"))
                .andExpect(jsonPath("$.ticketPrice").value(6000.0));
    }

    @Test
    @DisplayName("Should delete flight via DELETE endpoint")
    void testDeleteFlight() throws Exception {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightName("AI-101");
        flight.setSource("Delhi");
        flight.setDestination("Mumbai");
        flight.setTicketPrice(5000.0);
        Flight savedFlight = flightRepository.save(flight);

        // Act & Assert
        mockMvc.perform(delete("/flight/" + savedFlight.getFlightId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Flight ID: " + savedFlight.getFlightId())));
    }

    @Test
    @DisplayName("Should handle CORS for GET request")
    void testCorsHeaders() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/flight/")
                .header("Origin", "http://example.com"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }

    @Test
    @DisplayName("Should return empty list when no flights exist")
    void testGetAllFlights_Empty() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/flight/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should create multiple flights successfully")
    void testCreateMultipleFlights() throws Exception {
        // Arrange
        Flight flight1 = new Flight();
        flight1.setFlightName("AI-101");
        flight1.setSource("Delhi");
        flight1.setDestination("Mumbai");
        flight1.setTicketPrice(5000.0);

        Flight flight2 = new Flight();
        flight2.setFlightName("AI-102");
        flight2.setSource("Mumbai");
        flight2.setDestination("Bangalore");
        flight2.setTicketPrice(4500.0);

        // Act
        mockMvc.perform(post("/flight/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/flight/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight2)))
                .andExpect(status().isCreated());

        // Assert
        mockMvc.perform(get("/flight/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should handle update of non-existent flight")
    void testUpdateNonExistentFlight() throws Exception {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightName("AI-999");
        flight.setSource("Delhi");
        flight.setDestination("Mumbai");
        flight.setTicketPrice(5000.0);

        // Act & Assert
        mockMvc.perform(put("/flight/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isOk());
    }
}
