package com.airline.controller;

import com.airline.model.Flight;
        import com.airline.service.FlightService;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;

        import java.util.Arrays;
        import java.util.List;

        import static org.junit.jupiter.api.Assertions.assertEquals;
        import static org.mockito.Mockito.*;

class FlightControllerTest {

    @Mock
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFlight() {
        Flight flight = new Flight(); // Assuming Flight has a no-args constructor
        when(flightService.addFlight(flight)).thenReturn(flight);

        ResponseEntity<Flight> response = flightController.addFlight(flight);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(flight, response.getBody());
        verify(flightService, times(1)).addFlight(flight);
        verifyNoMoreInteractions(flightService);
    }

    @Test
    void testGetAllFlight() {
        List<Flight> flights = Arrays.asList(new Flight(), new Flight());
        when(flightService.getAllFlight()).thenReturn(flights);

        ResponseEntity<List<Flight>> response = flightController.getAllFlight();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
        verify(flightService, times(1)).getAllFlight();
        verifyNoMoreInteractions(flightService);
    }

    @Test
    void testGetFlight() throws Exception {
        int flightId = 1;
        Flight flight = new Flight();
        flight.setFlightId(flightId); // Assuming Flight class has setFlightId or an equivalent constructor
        when(flightService.getFlight(flightId)).thenReturn(flight);

        ResponseEntity<Flight> response = flightController.getFlight(flightId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
        verify(flightService, times(1)).getFlight(flightId);
        verifyNoMoreInteractions(flightService);
    }

    @Test
    void testDeleteFlight() {
        int flightId = 1;

        ResponseEntity<String> response = flightController.deleteFlight(flightId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Flight ID: " + flightId, response.getBody());
        verify(flightService, times(1)).deleteFlight(flightId);
        verifyNoMoreInteractions(flightService);
    }

    @Test
    void testUpdateFlight() throws Exception {
        int flightId = 1;
        Flight flight = new Flight();
        flight.setFlightId(flightId); // Ensure Flight class has setFlightId or use a constructor
        when(flightService.updateFlight(flightId, flight)).thenReturn(flight);

        ResponseEntity<Flight> response = flightController.updateFlight(flightId, flight);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
        verify(flightService, times(1)).updateFlight(flightId, flight);
        verifyNoMoreInteractions(flightService);
    }
}