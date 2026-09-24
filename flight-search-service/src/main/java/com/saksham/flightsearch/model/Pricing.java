package com.saksham.flightsearch.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pricing")
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatClass seatClass;

    @Column(nullable = false, length = 40)
    private String provider; // simulated GDS source, e.g. "PROVIDER_A"

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 3)
    private String currency = "INR";

    public Pricing() {}

    public Pricing(Flight flight, SeatClass seatClass, String provider, BigDecimal price, String currency) {
        this.flight = flight;
        this.seatClass = seatClass;
        this.provider = provider;
        this.price = price;
        this.currency = currency;
    }

    public Long getId() { return id; }
    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }
    public SeatClass getSeatClass() { return seatClass; }
    public void setSeatClass(SeatClass seatClass) { this.seatClass = seatClass; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
