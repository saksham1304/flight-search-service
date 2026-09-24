package com.saksham.flightsearch.gds;

import com.saksham.flightsearch.dto.FlightOffer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Real GDS integration: calls the Amadeus Self-Service Flight Offers Search
 * API (test environment) for live-ish fare data. Only active when
 * amadeus.enabled=true, so the app still runs with zero setup by default
 * using the simulated providers alone.
 *
 * Get free test credentials at https://developers.amadeus.com
 * Set env vars: AMADEUS_API_KEY, AMADEUS_API_SECRET, and amadeus.enabled=true
 */
@Component
@ConditionalOnProperty(name = "amadeus.enabled", havingValue = "true")
public class AmadeusProviderClient implements GdsProviderClient {

    private static final String PROVIDER_NAME = "AMADEUS_LIVE";

    private final AmadeusTokenService tokenService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${amadeus.search-url:https://test.api.amadeus.com/v2/shopping/flight-offers}")
    private String searchUrl;

    public AmadeusProviderClient(AmadeusTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public List<FlightOffer> searchOffers(String origin, String destination, LocalDate date) {
        List<FlightOffer> offers = new ArrayList<>();
        try {
            String url = UriComponentsBuilder.fromHttpUrl(searchUrl)
                    .queryParam("originLocationCode", origin)
                    .queryParam("destinationLocationCode", destination)
                    .queryParam("departureDate", date.toString())
                    .queryParam("adults", 1)
                    .queryParam("max", 3)
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenService.getAccessToken());
            HttpEntity<Void> request = new HttpEntity<>(headers);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET,
                    request, Map.class).getBody();

            if (response == null || !response.containsKey("data")) {
                return offers;
            }

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.get("data");

            for (Map<String, Object> offer : data) {
                offers.add(parseOffer(offer, origin, destination));
            }
        } catch (Exception e) {
            // Fail soft: if the live API is unreachable or credentials are wrong,
            // this provider simply contributes no offers rather than breaking the search.
            System.err.println("Amadeus provider call failed: " + e.getMessage());
        }
        return offers;
    }

    @SuppressWarnings("unchecked")
    private FlightOffer parseOffer(Map<String, Object> offer, String origin, String destination) {
        Map<String, Object> price = (Map<String, Object>) offer.get("price");
        BigDecimal total = new BigDecimal(price.get("total").toString());
        String currency = price.get("currency").toString();

        List<Map<String, Object>> itineraries = (List<Map<String, Object>>) offer.get("itineraries");
        Map<String, Object> firstItinerary = itineraries.get(0);
        List<Map<String, Object>> segments = (List<Map<String, Object>>) firstItinerary.get("segments");
        Map<String, Object> firstSegment = segments.get(0);
        Map<String, Object> lastSegment = segments.get(segments.size() - 1);

        String flightNumber = ((Map<String, Object>) firstSegment.get("carrierCode")) + ""
                + firstSegment.getOrDefault("number", "");
        String departureAt = (String) ((Map<String, Object>) firstSegment.get("departure")).get("at");
        String arrivalAt = (String) ((Map<String, Object>) lastSegment.get("arrival")).get("at");

        return new FlightOffer(
                flightNumber,
                String.valueOf(firstSegment.get("carrierCode")),
                origin,
                destination,
                LocalDateTime.parse(departureAt.substring(0, 19)),
                LocalDateTime.parse(arrivalAt.substring(0, 19)),
                "ECONOMY",
                total,
                currency,
                PROVIDER_NAME
        );
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
}
