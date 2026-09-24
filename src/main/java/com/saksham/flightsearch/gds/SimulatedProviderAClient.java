package com.saksham.flightsearch.gds;

import com.saksham.flightsearch.dto.FlightOffer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simulated GDS source "A". Generates a small, deterministic-ish set of
 * offers per route/date so repeated searches are cacheable and testable.
 */
@Component
public class SimulatedProviderAClient implements GdsProviderClient {

    private static final String PROVIDER_NAME = "PROVIDER_A";
    private final Random random = new Random();

    @Override
    public List<FlightOffer> searchOffers(String origin, String destination, LocalDate date) {
        List<FlightOffer> offers = new ArrayList<>();
        int flightCount = 2; // each simulated provider returns 2 flight options

        for (int i = 0; i < flightCount; i++) {
            String flightNumber = PROVIDER_NAME.substring(9) + (100 + random.nextInt(800));
            BigDecimal basePrice = BigDecimal.valueOf(3000 + random.nextInt(6000));
            LocalDateTime departure = date.atTime(6 + random.nextInt(14), 0);
            LocalDateTime arrival = departure.plusHours(1 + random.nextInt(3));

            offers.add(new FlightOffer(
                    flightNumber,
                    "Airline " + flightNumber.charAt(0),
                    origin,
                    destination,
                    departure,
                    arrival,
                    "ECONOMY",
                    basePrice,
                    "INR",
                    PROVIDER_NAME
            ));
        }
        return offers;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
}
