package com.saksham.flightsearch.controller;

import com.saksham.flightsearch.model.Route;
import com.saksham.flightsearch.repository.RouteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteRepository routeRepository;

    public RouteController(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @GetMapping
    public List<Route> getAll() {
        return routeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> getById(@PathVariable Long id) {
        return routeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Route create(@Valid @RequestBody Route route) {
        return routeRepository.save(route);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
