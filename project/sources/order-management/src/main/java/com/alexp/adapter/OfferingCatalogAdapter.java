package com.alexp.adapter;

import com.alexp.model.Offering;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class OfferingCatalogAdapter {

    private static final String OFFERING_CATALOG_URL = "localhost:8001";

    private final RestTemplate restTemplate;

    public OfferingCatalogAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Offering getOfferingById(UUID offeringId) {
        Offering offering = restTemplate.getForObject(
                "http://"+ OFFERING_CATALOG_URL +"/api/v1/offerings/"+offeringId,
                Offering.class
        );
        return offering;
    }

}
