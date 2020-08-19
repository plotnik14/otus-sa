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
        iniDB();
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
        return ResponseEntity.ok(offerings);
    }

    @PostMapping
    public ResponseEntity<Offering> createNewOffering(@RequestBody Offering offering) {
        offering.setOfferingId(null);
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


    private void iniDB() {
        Offering offering = new Offering();
        offering.setOfferingId(UUID.fromString("8d68f7be-e24d-11ea-87d0-0242ac130003"));
        offering.setName("VESTON F-38/BK");
        offering.setPrice(6400.0);
        offering.setDescription("VESTON F-38/BK Desc");
        offering.setStatus(OfferingStatus.ACTIVE.getName());
        offeringRepository.save(offering);

        offering = new Offering();
        offering.setOfferingId(UUID.fromString("995b0cba-e24d-11ea-87d0-0242ac130003"));
        offering.setName("MARTIN LX BLACK");
        offering.setPrice(38990.0);
        offering.setDescription("MARTIN LX BLACK Desc");
        offering.setStatus(OfferingStatus.ACTIVE.getName());
        offeringRepository.save(offering);

        offering = new Offering();
        offering.setOfferingId(UUID.fromString("9f4bb246-e24d-11ea-87d0-0242ac130003"));
        offering.setName("FENDER FA-125 DREADNOUGHT WALNUT");
        offering.setPrice(12240.0);
        offering.setDescription("FENDER FA-125 DREADNOUGHT WALNUT Desc");
        offering.setStatus(OfferingStatus.ACTIVE.getName());
        offeringRepository.save(offering);
    }
}
