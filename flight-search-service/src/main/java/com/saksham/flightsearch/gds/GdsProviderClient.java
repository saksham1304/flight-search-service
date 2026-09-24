package com.saksham.flightsearch.gds;

import com.saksham.flightsearch.dto.FlightOffer;
import java.time.LocalDate;
import java.util.List;

/**
 * Contract every simulated GDS (Global Distribution System) source implements.
 * In production these would be real HTTP/SOAP calls to Amadeus, Sabre, Travelport etc.
 * Here they are simulated so the aggregation/caching logic can be built and tested
 * without needing live third-party credentials.
 */
public interface GdsProviderClient {
    List<FlightOffer> searchOffers(String origin, String destination, LocalDate date);
    String getProviderName();
}
