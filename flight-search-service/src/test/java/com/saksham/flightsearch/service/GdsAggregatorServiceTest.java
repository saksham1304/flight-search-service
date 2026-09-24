package com.saksham.flightsearch.service;

import com.saksham.flightsearch.dto.FlightOffer;
import com.saksham.flightsearch.gds.GdsProviderClient;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GdsAggregatorServiceTest {

    @Test
    void aggregateOffers_returnsOffersSortedCheapestFirst() {
        GdsProviderClient providerA = mock(GdsProviderClient.class);
        GdsProviderClient providerB = mock(GdsProviderClient.class);

        LocalDate date = LocalDate.of(2026, 10, 1);
        LocalDateTime dep = date.atTime(9, 0);
        LocalDateTime arr = date.atTime(11, 0);

        when(providerA.searchOffers("DEL", "BOM", date)).thenReturn(List.of(
                new FlightOffer("A101", "Airline A", "DEL", "BOM", dep, arr, "ECONOMY",
                        BigDecimal.valueOf(5000), "INR", "PROVIDER_A")
        ));
        when(providerB.searchOffers("DEL", "BOM", date)).thenReturn(List.of(
                new FlightOffer("B202", "Airline B", "DEL", "BOM", dep, arr, "ECONOMY",
                        BigDecimal.valueOf(3500), "INR", "PROVIDER_B")
        ));

        GdsAggregatorService aggregator = new GdsAggregatorService(List.of(providerA, providerB));
        List<FlightOffer> offers = aggregator.aggregateOffers("DEL", "BOM", date);

        assertEquals(2, offers.size());
        assertEquals("B202", offers.get(0).getFlightNumber()); // cheapest first
        assertEquals("A101", offers.get(1).getFlightNumber());
    }

    @Test
    void findCheapestOffer_returnsLowestPricedOffer() {
        GdsProviderClient provider = mock(GdsProviderClient.class);
        LocalDate date = LocalDate.of(2026, 11, 15);
        LocalDateTime dep = date.atTime(10, 0);
        LocalDateTime arr = date.atTime(12, 0);

        when(provider.searchOffers("BLR", "DEL", date)).thenReturn(List.of(
                new FlightOffer("C303", "Airline C", "BLR", "DEL", dep, arr, "ECONOMY",
                        BigDecimal.valueOf(4200), "INR", "PROVIDER_C"),
                new FlightOffer("C304", "Airline C", "BLR", "DEL", dep, arr, "ECONOMY",
                        BigDecimal.valueOf(3900), "INR", "PROVIDER_C")
        ));

        GdsAggregatorService aggregator = new GdsAggregatorService(List.of(provider));
        FlightOffer cheapest = aggregator.findCheapestOffer("BLR", "DEL", date);

        assertNotNull(cheapest);
        assertEquals("C304", cheapest.getFlightNumber());
        assertEquals(BigDecimal.valueOf(3900), cheapest.getPrice());
    }

    @Test
    void findCheapestOffer_returnsNull_whenNoProvidersReturnOffers() {
        GdsProviderClient provider = mock(GdsProviderClient.class);
        LocalDate date = LocalDate.of(2026, 12, 25);
        when(provider.searchOffers("GOI", "DEL", date)).thenReturn(List.of());

        GdsAggregatorService aggregator = new GdsAggregatorService(List.of(provider));
        assertNull(aggregator.findCheapestOffer("GOI", "DEL", date));
    }
}
