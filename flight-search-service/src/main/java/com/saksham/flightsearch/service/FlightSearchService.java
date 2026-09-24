package com.saksham.flightsearch.service;

import com.saksham.flightsearch.dto.FlightOffer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Public-facing search service. The @Cacheable annotation is backed by Redis
 * in the "prod" profile (see CacheConfig) so repeated searches for the same
 * route + date skip redundant simulated-supplier calls until the TTL expires.
 */
@Service
public class FlightSearchService {

    private final GdsAggregatorService aggregatorService;

    public FlightSearchService(GdsAggregatorService aggregatorService) {
        this.aggregatorService = aggregatorService;
    }

    @Cacheable(value = "flightOffers", key = "#origin + '-' + #destination + '-' + #date")
    public List<FlightOffer> search(String origin, String destination, LocalDate date) {
        return aggregatorService.aggregateOffers(origin, destination, date);
    }

    @Cacheable(value = "cheapestFare", key = "#origin + '-' + #destination + '-' + #date")
    public FlightOffer findCheapestFare(String origin, String destination, LocalDate date) {
        return aggregatorService.findCheapestOffer(origin, destination, date);
    }
}
