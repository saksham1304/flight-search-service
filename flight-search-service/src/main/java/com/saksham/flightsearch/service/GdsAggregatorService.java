package com.saksham.flightsearch.service;

import com.saksham.flightsearch.dto.FlightOffer;
import com.saksham.flightsearch.gds.GdsProviderClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * Calls every registered simulated GDS provider, normalizes their responses
 * into a single FlightOffer shape, and returns them sorted cheapest-first.
 */
@Service
public class GdsAggregatorService {

    private final List<GdsProviderClient> providers;

    public GdsAggregatorService(List<GdsProviderClient> providers) {
        this.providers = providers;
    }

    public List<FlightOffer> aggregateOffers(String origin, String destination, LocalDate date) {
        return providers.stream()
                .flatMap(provider -> provider.searchOffers(origin, destination, date).stream())
                .sorted(Comparator.comparing(FlightOffer::getPrice))
                .toList();
    }

    public FlightOffer findCheapestOffer(String origin, String destination, LocalDate date) {
        return aggregateOffers(origin, destination, date).stream()
                .findFirst()
                .orElse(null);
    }
}
