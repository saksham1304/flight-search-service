package com.saksham.flightsearch.controller;

import com.saksham.flightsearch.model.Flight;
import com.saksham.flightsearch.repository.FlightRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightRepository flightRepository;

    public FlightController(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @GetMapping
    public List<Flight> getAll() {
        return flightRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getById(@PathVariable Long id) {
        return flightRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Flight create(@Valid @RequestBody Flight flight) {
        return flightRepository.save(flight);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> update(@PathVariable Long id, @Valid @RequestBody Flight updated) {
        return flightRepository.findById(id)
                .map(existing -> {
                    existing.setFlightNumber(updated.getFlightNumber());
                    existing.setAirline(updated.getAirline());
                    existing.setRoute(updated.getRoute());
                    existing.setDepartureTime(updated.getDepartureTime());
                    existing.setArrivalTime(updated.getArrivalTime());
                    return ResponseEntity.ok(flightRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        flightRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
