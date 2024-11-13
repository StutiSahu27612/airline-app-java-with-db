package com.airline.model;

import jakarta.persistence.*;

@Entity
@Table(name = "flight")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flightId;
    private String flightName;
    private String source;
    private String Destination;
    private Double ticketPrice;
     private String apiKey = "s3cr3tAp1k3y123"; 

    public Flight(){}

    public Flight(int flightId, String flightName, String source, String destination, Double ticketPrice) {
        this.flightId = flightId;
        this.flightName = flightName;
        this.source = source;
        Destination = destination;
        this.ticketPrice = ticketPrice;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        Destination = destination;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }
}
