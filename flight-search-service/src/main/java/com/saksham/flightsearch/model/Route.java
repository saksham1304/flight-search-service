package com.saksham.flightsearch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "routes", indexes = {
        @Index(name = "idx_route_origin_dest", columnList = "origin,destination")
})
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 3)
    private String origin;

    @Column(nullable = false, length = 3)
    private String destination;

    private Integer distanceKm;

    public Route() {}

    public Route(String origin, String destination, Integer distanceKm) {
        this.origin = origin;
        this.destination = destination;
        this.distanceKm = distanceKm;
    }

    public Long getId() { return id; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Integer getDistanceKm() { return distanceKm; }
    public void setDistanceKm(Integer distanceKm) { this.distanceKm = distanceKm; }
}
