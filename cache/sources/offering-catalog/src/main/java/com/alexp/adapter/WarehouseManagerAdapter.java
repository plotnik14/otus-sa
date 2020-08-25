package com.alexp.adapter;

import com.alexp.model.AvailabilityCheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class WarehouseManagerAdapter {
    private final static Logger LOGGER = LoggerFactory.getLogger(WarehouseManagerAdapter.class);

    private static final String WAREHOUSE_MANAGEMENT_URL = "warehouse-management:9000";

    private final RestTemplate restTemplate;

    public WarehouseManagerAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AvailabilityCheckResult checkOfferingAvailability(UUID offeringId) {
        try {
            return restTemplate.getForObject(
                    "http://" + WAREHOUSE_MANAGEMENT_URL + "/api/v1/availability?offeringId=" + offeringId,
                    AvailabilityCheckResult.class
            );
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return null;
        }
    }
}
