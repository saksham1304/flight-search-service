package com.saksham.flightsearch.repository;

import com.saksham.flightsearch.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PricingRepository extends JpaRepository<Pricing, Long> {
    List<Pricing> findByFlight_Id(Long flightId);
}
