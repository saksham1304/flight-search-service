package com.saksham.flightsearch.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * Normalized offer returned after aggregating and de-duplicating
 * responses from multiple simulated GDS provider sources.
 */
public class FlightOffer implements Serializable {

    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String seatClass;
    private BigDecimal price;
    private String currency;
    private String sourceProvider;

    public FlightOffer() {}

    public FlightOffer(String flightNumber, String airline, String origin, String destination,
                        LocalDateTime departureTime, LocalDateTime arrivalTime, String seatClass,
                        BigDecimal price, String currency, String sourceProvider) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatClass = seatClass;
        this.price = price;
        this.currency = currency;
        this.sourceProvider = sourceProvider;
    }

    public String getFlightNumber() { return flightNumber; }
    public String getAirline() { return airline; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public String getSeatClass() { return seatClass; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getSourceProvider() { return sourceProvider; }
}
