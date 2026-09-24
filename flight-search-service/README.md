# Flight Search & Price Caching Service

A RESTful backend that aggregates flight availability from multiple simulated GDS
(Global Distribution System) sources, normalizes the responses, and returns the
cheapest fare. Repeated searches for the same route + date are served from a
Redis cache with TTL-based expiry instead of hitting the simulated suppliers again.

## Tech Stack
Java 17 · Spring Boot 3 · Spring Data JPA · Spring Cache (Redis) · MySQL · H2 (local) · Spring Security · JUnit 5 + Mockito

## Architecture
```
Client -> SearchController -> FlightSearchService (Redis @Cacheable)
                                    -> GdsAggregatorService
                                         -> SimulatedProviderAClient
                                         -> SimulatedProviderBClient
                                         -> SimulatedProviderCClient
```
Each `SimulatedProvider*Client` stands in for a real GDS integration (Amadeus,
Sabre, Travelport, etc.) so the aggregation, normalization, and caching logic
can be built and tested without live third-party credentials.

**Schema:** `routes` → `flights` → `seats` / `pricing` (see `model/` package),
modeled in MySQL for production use.

## Running locally (no MySQL/Redis required)
```bash
mvn spring-boot:run
```
This uses the `local` profile by default: an in-memory H2 database and a simple
in-memory cache, so you can run and test the whole thing with zero external setup.

- API base: `http://localhost:8080`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:flightdb`)

## Running with real MySQL + Redis (prod profile)
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_HOST=localhost DB_NAME=flightdb DB_USERNAME=root DB_PASSWORD=yourpassword
export REDIS_HOST=localhost REDIS_PORT=6379
export ADMIN_USERNAME=admin ADMIN_PASSWORD=yourStrongPassword
mvn spring-boot:run
```

## Running tests
```bash
mvn test
```

## API Endpoints

| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/search?origin=DEL&destination=BOM&date=2026-10-01` | Public | All offers, cheapest first (cached) |
| GET | `/api/search/cheapest?origin=DEL&destination=BOM&date=2026-10-01` | Public | Single cheapest fare (cached) |
| GET | `/api/routes` | Public | List routes |
| POST | `/api/routes` | Basic auth | Create a route |
| GET | `/api/flights` | Public | List flights |
| POST | `/api/flights` | Basic auth | Create a flight |
| PUT | `/api/flights/{id}` | Basic auth | Update a flight |
| DELETE | `/api/flights/{id}` | Basic auth | Delete a flight |

Example:
```bash
curl "http://localhost:8080/api/search/cheapest?origin=DEL&destination=BOM&date=2026-10-01"
```

## Docker
```bash
docker build -t flight-search-service .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=<host> -e DB_NAME=flightdb -e DB_USERNAME=<user> -e DB_PASSWORD=<pass> \
  -e REDIS_HOST=<host> -e REDIS_PORT=6379 \
  flight-search-service
```

## CI
Every push to `main` runs `.github/workflows/ci.yml`, which builds the project
and runs the full test suite on GitHub Actions.
