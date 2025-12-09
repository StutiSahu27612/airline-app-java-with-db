package com.airline.repository;

import com.airline.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Flight Repository Tests")
class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    private Flight testFlight1;
    private Flight testFlight2;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        flightRepository.deleteAll();
        
        testFlight1 = new Flight();
        testFlight1.setFlightName("AI-101");
        testFlight1.setSource("Delhi");
        testFlight1.setDestination("Mumbai");
        testFlight1.setTicketPrice(5000.0);

        testFlight2 = new Flight();
        testFlight2.setFlightName("AI-102");
        testFlight2.setSource("Mumbai");
        testFlight2.setDestination("Bangalore");
        testFlight2.setTicketPrice(4500.0);
    }

    @Test
    @DisplayName("Should save flight successfully")
    void testSaveFlight() {
        // Act
        Flight savedFlight = flightRepository.save(testFlight1);

        // Assert
        assertNotNull(savedFlight);
        assertNotNull(savedFlight.getFlightId());
        assertEquals(testFlight1.getFlightName(), savedFlight.getFlightName());
        assertEquals(testFlight1.getSource(), savedFlight.getSource());
        assertEquals(testFlight1.getDestination(), savedFlight.getDestination());
        assertEquals(testFlight1.getTicketPrice(), savedFlight.getTicketPrice());
    }

    @Test
    @DisplayName("Should find flight by ID")
    void testFindById() {
        // Arrange
        Flight savedFlight = flightRepository.save(testFlight1);

        // Act
        Optional<Flight> foundFlight = flightRepository.findById(savedFlight.getFlightId());

        // Assert
        assertTrue(foundFlight.isPresent());
        assertEquals(savedFlight.getFlightId(), foundFlight.get().getFlightId());
        assertEquals(savedFlight.getFlightName(), foundFlight.get().getFlightName());
    }

    @Test
    @DisplayName("Should return empty when flight not found by ID")
    void testFindById_NotFound() {
        // Act
        Optional<Flight> foundFlight = flightRepository.findById(999);

        // Assert
        assertFalse(foundFlight.isPresent());
    }

    @Test
    @DisplayName("Should find all flights")
    void testFindAll() {
        // Arrange
        flightRepository.save(testFlight1);
        flightRepository.save(testFlight2);

        // Act
        List<Flight> flights = flightRepository.findAll();

        // Assert
        assertNotNull(flights);
        assertEquals(2, flights.size());
    }

    @Test
    @DisplayName("Should return empty list when no flights exist")
    void testFindAll_Empty() {
        // Act
        List<Flight> flights = flightRepository.findAll();

        // Assert
        assertNotNull(flights);
        assertTrue(flights.isEmpty());
    }

    @Test
    @DisplayName("Should delete flight by ID")
    void testDeleteById() {
        // Arrange
        Flight savedFlight = flightRepository.save(testFlight1);
        int flightId = savedFlight.getFlightId();

        // Act
        flightRepository.deleteById(flightId);

        // Assert
        Optional<Flight> deletedFlight = flightRepository.findById(flightId);
        assertFalse(deletedFlight.isPresent());
    }

    @Test
    @DisplayName("Should delete all flights")
    void testDeleteAll() {
        // Arrange
        flightRepository.save(testFlight1);
        flightRepository.save(testFlight2);

        // Act
        flightRepository.deleteAll();

        // Assert
        List<Flight> flights = flightRepository.findAll();
        assertTrue(flights.isEmpty());
    }

    @Test
    @DisplayName("Should update existing flight")
    void testUpdateFlight() {
        // Arrange
        Flight savedFlight = flightRepository.save(testFlight1);
        int flightId = savedFlight.getFlightId();

        // Act
        savedFlight.setFlightName("AI-101-Updated");
        savedFlight.setTicketPrice(6000.0);
        Flight updatedFlight = flightRepository.save(savedFlight);

        // Assert
        assertEquals(flightId, updatedFlight.getFlightId());
        assertEquals("AI-101-Updated", updatedFlight.getFlightName());
        assertEquals(6000.0, updatedFlight.getTicketPrice());
    }

    @Test
    @DisplayName("Should count flights correctly")
    void testCount() {
        // Arrange
        flightRepository.save(testFlight1);
        flightRepository.save(testFlight2);

        // Act
        long count = flightRepository.count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should check if flight exists by ID")
    void testExistsById() {
        // Arrange
        Flight savedFlight = flightRepository.save(testFlight1);

        // Act & Assert
        assertTrue(flightRepository.existsById(savedFlight.getFlightId()));
        assertFalse(flightRepository.existsById(999));
    }

    @Test
    @DisplayName("Should save flight with null optional fields")
    void testSaveFlightWithNullFields() {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightName("AI-103");
        // Not setting source, destination, or price

        // Act
        Flight savedFlight = flightRepository.save(flight);

        // Assert
        assertNotNull(savedFlight);
        assertNotNull(savedFlight.getFlightId());
        assertEquals("AI-103", savedFlight.getFlightName());
        assertNull(savedFlight.getSource());
        assertNull(savedFlight.getDestination());
        assertNull(savedFlight.getTicketPrice());
    }

    @Test
    @DisplayName("Should handle multiple saves of same flight")
    void testMultipleSaves() {
        // Arrange & Act
        Flight savedFlight1 = flightRepository.save(testFlight1);
        int originalId = savedFlight1.getFlightId();
        
        savedFlight1.setTicketPrice(5500.0);
        Flight savedFlight2 = flightRepository.save(savedFlight1);

        // Assert
        assertEquals(originalId, savedFlight2.getFlightId());
        assertEquals(5500.0, savedFlight2.getTicketPrice());
        assertEquals(1, flightRepository.count());
    }

    @Test
    @DisplayName("Should persist flight attributes correctly")
    void testFlightAttributePersistence() {
        // Arrange
        Flight flight = new Flight();
        flight.setFlightName("TEST-999");
        flight.setSource("TestCity1");
        flight.setDestination("TestCity2");
        flight.setTicketPrice(9999.99);

        // Act
        Flight savedFlight = flightRepository.save(flight);
        Optional<Flight> retrievedFlight = flightRepository.findById(savedFlight.getFlightId());

        // Assert
        assertTrue(retrievedFlight.isPresent());
        Flight flight1 = retrievedFlight.get();
        assertEquals("TEST-999", flight1.getFlightName());
        assertEquals("TestCity1", flight1.getSource());
        assertEquals("TestCity2", flight1.getDestination());
        assertEquals(9999.99, flight1.getTicketPrice());
    }
}
