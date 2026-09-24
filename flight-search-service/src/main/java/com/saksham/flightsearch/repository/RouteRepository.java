package com.saksham.flightsearch.repository;

import com.saksham.flightsearch.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByOriginAndDestination(String origin, String destination);
}
