package com.airline.service;

import com.airline.model.Flight;
import com.airline.repository.FlightRepository;
import com.airline.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@DisplayName("Flight Service Implementation Tests")
class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    private Flight testFlight;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testFlight = new Flight(1, "AI-101", "Delhi", "Mumbai", 5000.0);
    }

    @Test
    @DisplayName("Should add a new flight successfully")
    void testAddFlight_Success() {
        // Arrange
        when(flightRepository.save(any(Flight.class))).thenReturn(testFlight);

        // Act
        Flight result = flightService.addFlight(testFlight);

        // Assert
        assertNotNull(result);
        assertEquals(testFlight.getFlightId(), result.getFlightId());
        assertEquals(testFlight.getFlightName(), result.getFlightName());
        assertEquals(testFlight.getSource(), result.getSource());
        assertEquals(testFlight.getDestination(), result.getDestination());
        assertEquals(testFlight.getTicketPrice(), result.getTicketPrice());
        verify(flightRepository, times(1)).save(testFlight);
    }

    @Test
    @DisplayName("Should return all flights")
    void testGetAllFlight_Success() {
        // Arrange
        Flight flight2 = new Flight(2, "AI-102", "Mumbai", "Bangalore", 4500.0);
        List<Flight> flights = Arrays.asList(testFlight, flight2);
        when(flightRepository.findAll()).thenReturn(flights);

        // Act
        List<Flight> result = flightService.getAllFlight();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testFlight.getFlightName(), result.get(0).getFlightName());
        assertEquals(flight2.getFlightName(), result.get(1).getFlightName());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no flights exist")
    void testGetAllFlight_EmptyList() {
        // Arrange
        when(flightRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Flight> result = flightService.getAllFlight();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get flight by ID successfully")
    void testGetFlight_Success() throws Exception {
        // Arrange
        when(flightRepository.findById(1)).thenReturn(Optional.of(testFlight));

        // Act
        Flight result = flightService.getFlight(1);

        // Assert
        assertNotNull(result);
        assertEquals(testFlight.getFlightId(), result.getFlightId());
        assertEquals(testFlight.getFlightName(), result.getFlightName());
        verify(flightRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should throw exception when flight not found")
    void testGetFlight_NotFound() {
        // Arrange
        when(flightRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            flightService.getFlight(999);
        });

        assertTrue(exception.getMessage().contains("No Flight with Id: 999"));
        verify(flightRepository, times(1)).findById(999);
    }

    @Test
    @DisplayName("Should delete flight successfully")
    void testDeleteFlight_Success() {
        // Arrange
        doNothing().when(flightRepository).deleteById(1);

        // Act
        boolean result = flightService.deleteFlight(1);

        // Assert
        assertTrue(result);
        verify(flightRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should update flight successfully")
    void testUpdateFlight_Success() {
        // Arrange
        Flight updatedFlight = new Flight(1, "AI-101-Updated", "Delhi", "Chennai", 6000.0);
        when(flightRepository.save(any(Flight.class))).thenReturn(updatedFlight);

        // Act
        Flight result = flightService.updateFlight(1, updatedFlight);

        // Assert
        assertNotNull(result);
        assertEquals(updatedFlight.getFlightId(), result.getFlightId());
        assertEquals(updatedFlight.getFlightName(), result.getFlightName());
        assertEquals(updatedFlight.getDestination(), result.getDestination());
        assertEquals(updatedFlight.getTicketPrice(), result.getTicketPrice());
        verify(flightRepository, times(1)).save(updatedFlight);
    }

    @Test
    @DisplayName("Should handle null flight when adding")
    void testAddFlight_NullFlight() {
        // Arrange
        when(flightRepository.save(null)).thenThrow(new IllegalArgumentException("Flight cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            flightService.addFlight(null);
        });
    }

    @Test
    @DisplayName("Should verify repository method is called when deleting")
    void testDeleteFlight_VerifyRepositoryCall() {
        // Arrange
        int flightId = 5;
        doNothing().when(flightRepository).deleteById(flightId);

        // Act
        flightService.deleteFlight(flightId);

        // Assert
        verify(flightRepository, times(1)).deleteById(flightId);
    }
}
