package com.saksham.flightsearch.repository;

import com.saksham.flightsearch.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByRoute_OriginAndRoute_DestinationAndDepartureTimeBetween(
            String origin, String destination, LocalDateTime start, LocalDateTime end);
}
