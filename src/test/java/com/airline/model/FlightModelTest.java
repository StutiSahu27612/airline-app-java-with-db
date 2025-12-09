package com.airline.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Flight Model Tests")
class FlightModelTest {

    private Flight flight;

    @BeforeEach
    void setUp() {
        flight = new Flight();
    }

    @Test
    @DisplayName("Should create flight with no-args constructor")
    void testNoArgsConstructor() {
        // Assert
        assertNotNull(flight);
    }

    @Test
    @DisplayName("Should create flight with all-args constructor")
    void testAllArgsConstructor() {
        // Act
        Flight flight = new Flight(1, "AI-101", "Delhi", "Mumbai", 5000.0);

        // Assert
        assertNotNull(flight);
        assertEquals(1, flight.getFlightId());
        assertEquals("AI-101", flight.getFlightName());
        assertEquals("Delhi", flight.getSource());
        assertEquals("Mumbai", flight.getDestination());
        assertEquals(5000.0, flight.getTicketPrice());
    }

    @Test
    @DisplayName("Should set and get flight ID")
    void testSetAndGetFlightId() {
        // Act
        flight.setFlightId(10);

        // Assert
        assertEquals(10, flight.getFlightId());
    }

    @Test
    @DisplayName("Should set and get flight name")
    void testSetAndGetFlightName() {
        // Act
        flight.setFlightName("AI-202");

        // Assert
        assertEquals("AI-202", flight.getFlightName());
    }

    @Test
    @DisplayName("Should set and get source")
    void testSetAndGetSource() {
        // Act
        flight.setSource("Bangalore");

        // Assert
        assertEquals("Bangalore", flight.getSource());
    }

    @Test
    @DisplayName("Should set and get destination")
    void testSetAndGetDestination() {
        // Act
        flight.setDestination("Chennai");

        // Assert
        assertEquals("Chennai", flight.getDestination());
    }

    @Test
    @DisplayName("Should set and get ticket price")
    void testSetAndGetTicketPrice() {
        // Act
        flight.setTicketPrice(7500.0);

        // Assert
        assertEquals(7500.0, flight.getTicketPrice());
    }

    @Test
    @DisplayName("Should handle null flight name")
    void testNullFlightName() {
        // Act
        flight.setFlightName(null);

        // Assert
        assertNull(flight.getFlightName());
    }

    @Test
    @DisplayName("Should handle null source")
    void testNullSource() {
        // Act
        flight.setSource(null);

        // Assert
        assertNull(flight.getSource());
    }

    @Test
    @DisplayName("Should handle null destination")
    void testNullDestination() {
        // Act
        flight.setDestination(null);

        // Assert
        assertNull(flight.getDestination());
    }

    @Test
    @DisplayName("Should handle null ticket price")
    void testNullTicketPrice() {
        // Act
        flight.setTicketPrice(null);

        // Assert
        assertNull(flight.getTicketPrice());
    }

    @Test
    @DisplayName("Should handle zero ticket price")
    void testZeroTicketPrice() {
        // Act
        flight.setTicketPrice(0.0);

        // Assert
        assertEquals(0.0, flight.getTicketPrice());
    }

    @Test
    @DisplayName("Should handle negative ticket price")
    void testNegativeTicketPrice() {
        // Act
        flight.setTicketPrice(-100.0);

        // Assert
        assertEquals(-100.0, flight.getTicketPrice());
    }

    @Test
    @DisplayName("Should handle large ticket price")
    void testLargeTicketPrice() {
        // Act
        flight.setTicketPrice(999999.99);

        // Assert
        assertEquals(999999.99, flight.getTicketPrice());
    }

    @Test
    @DisplayName("Should handle empty string for flight name")
    void testEmptyFlightName() {
        // Act
        flight.setFlightName("");

        // Assert
        assertEquals("", flight.getFlightName());
    }

    @Test
    @DisplayName("Should handle empty string for source")
    void testEmptySource() {
        // Act
        flight.setSource("");

        // Assert
        assertEquals("", flight.getSource());
    }

    @Test
    @DisplayName("Should handle empty string for destination")
    void testEmptyDestination() {
        // Act
        flight.setDestination("");

        // Assert
        assertEquals("", flight.getDestination());
    }

    @Test
    @DisplayName("Should set all properties correctly")
    void testSetAllProperties() {
        // Act
        flight.setFlightId(99);
        flight.setFlightName("TEST-999");
        flight.setSource("Hyderabad");
        flight.setDestination("Pune");
        flight.setTicketPrice(8500.0);

        // Assert
        assertEquals(99, flight.getFlightId());
        assertEquals("TEST-999", flight.getFlightName());
        assertEquals("Hyderabad", flight.getSource());
        assertEquals("Pune", flight.getDestination());
        assertEquals(8500.0, flight.getTicketPrice());
    }
}
