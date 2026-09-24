package com.saksham.flightsearch.controller;

import com.saksham.flightsearch.dto.FlightOffer;
import com.saksham.flightsearch.service.FlightSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final FlightSearchService flightSearchService;

    public SearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    /** GET /api/search?origin=DEL&destination=BOM&date=2026-10-01 */
    @GetMapping
    public List<FlightOffer> search(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightSearchService.search(origin.toUpperCase(), destination.toUpperCase(), date);
    }

    /** GET /api/search/cheapest?origin=DEL&destination=BOM&date=2026-10-01 */
    @GetMapping("/cheapest")
    public FlightOffer cheapest(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightSearchService.findCheapestFare(origin.toUpperCase(), destination.toUpperCase(), date);
    }
}
