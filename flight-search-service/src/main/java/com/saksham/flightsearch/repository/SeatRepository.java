package com.saksham.flightsearch.repository;

import com.saksham.flightsearch.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByFlight_Id(Long flightId);
}
