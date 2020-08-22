package com.alexp.controller;

import com.alexp.adapter.WarehouseManagerAdapter;
import com.alexp.model.AvailabilityCheckResult;
import com.alexp.model.ChangeStatusRequest;
import com.alexp.model.Offering;
import com.alexp.model.OfferingStatus;
import com.alexp.repository.OfferingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/offerings")
public class OfferingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferingController.class);

    private final OfferingRepository offeringRepository;
    private final WarehouseManagerAdapter warehouseManagerAdapter;

    public OfferingController(OfferingRepository offeringRepository,
                              WarehouseManagerAdapter warehouseManagerAdapter) {
        this.offeringRepository = offeringRepository;
        this.warehouseManagerAdapter = warehouseManagerAdapter;
    }

    // Testing only
    @GetMapping
    public ResponseEntity<Iterable<Offering>> getAllOfferings() {
        Iterable<Offering> offerings = offeringRepository.findAll();
        return ResponseEntity.ok(offerings);
    }

    @GetMapping("/{offeringId}")
    public ResponseEntity<Offering> getOffering(@PathVariable("offeringId") UUID offeringId) {
        Offering offering = offeringRepository.findById(offeringId).orElse(null);

        if (offering == null){
            return ResponseEntity.notFound().build();
        }

        AvailabilityCheckResult availabilityCheckResult = warehouseManagerAdapter.checkOfferingAvailability(offeringId);

        LOGGER.debug("AvailabilityCheckResult:{}", availabilityCheckResult);

        if(availabilityCheckResult == null) {
            offering.setAvailable(0L);
        } else {
            offering.setAvailable(availabilityCheckResult.getTotalCount());
        }

        return ResponseEntity.ok(offering);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Offering>> getOfferingsByParams(@RequestParam("query") String query) {
        List<Offering> offerings = offeringRepository.findAllByNameContains(query);

        offerings = offerings.stream()
                .filter(offering -> OfferingStatus.ACTIVE.getName().equals(offering.getStatus()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(offerings);
    }

    @PostMapping
    public ResponseEntity<Offering> createNewOffering(@RequestBody Offering offering) {
        offering.setOfferingId(UUID.randomUUID());
        offering.setStatus(OfferingStatus.IN_DEVELOPMENT.getName());
        Offering createdOffering = offeringRepository.save(offering);
        return ResponseEntity.ok(createdOffering);
    }

    @PostMapping("/{offeringId}/changeStatus")
    public ResponseEntity<Offering> changeOfferingStatus(@PathVariable("offeringId") UUID offeringId,
                                                         @RequestBody ChangeStatusRequest changeStatusRequest) {
        Offering offering = offeringRepository.findById(offeringId).orElse(null);

        if (offering == null){
            return ResponseEntity.notFound().build();
        }

        // ToDo Transition validation

        offering.setStatus(changeStatusRequest.getStatus());
        offering = offeringRepository.save(offering);

        return ResponseEntity.ok(offering);
    }

    @PutMapping("/{offeringId}")
    public ResponseEntity<Offering> updateOffering(@PathVariable("offeringId") UUID offeringId,
                                                   @RequestBody Offering offering) {
        Offering offeringToUpdate = offeringRepository.findById(offeringId).orElse(null);

        if (offeringToUpdate == null){
            return ResponseEntity.notFound().build();
        }

        // ToDo status validation

        if (offering.getName() != null) {
            offeringToUpdate.setName(offering.getName());
        }

        if (offering.getDescription() != null) {
            offeringToUpdate.setDescription(offering.getDescription());
        }

        if (offering.getPrice() != null) {
            offeringToUpdate.setPrice(offering.getPrice());
        }

        offeringToUpdate = offeringRepository.save(offeringToUpdate);

        return ResponseEntity.ok(offeringToUpdate);
    }
}
