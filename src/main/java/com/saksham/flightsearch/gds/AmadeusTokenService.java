package com.saksham.flightsearch.gds;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

/**
 * Fetches and caches an OAuth2 client-credentials access token from Amadeus's
 * Self-Service test API. Tokens are valid for ~30 minutes; this refreshes
 * automatically a few seconds before expiry.
 */
@Service
@ConditionalOnProperty(name = "amadeus.enabled", havingValue = "true")
public class AmadeusTokenService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${amadeus.api-key:}")
    private String apiKey;

    @Value("${amadeus.api-secret:}")
    private String apiSecret;

    @Value("${amadeus.auth-url:https://test.api.amadeus.com/v1/security/oauth2/token}")
    private String authUrl;

    private String cachedToken;
    private Instant expiresAt = Instant.EPOCH;

    public synchronized String getAccessToken() {
        if (cachedToken != null && Instant.now().isBefore(expiresAt.minusSeconds(10))) {
            return cachedToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", apiKey);
        body.add("client_secret", apiSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(authUrl, request, Map.class);

        if (response == null || !response.containsKey("access_token")) {
            throw new IllegalStateException("Failed to obtain Amadeus access token");
        }

        cachedToken = (String) response.get("access_token");
        int expiresIn = (int) response.getOrDefault("expires_in", 1800);
        expiresAt = Instant.now().plusSeconds(expiresIn);

        return cachedToken;
    }
}
