package com.alexp.controller;

import com.alexp.model.ChangeStatusRequest;
import com.alexp.model.Offering;
import com.alexp.service.OfferingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/offerings")
public class OfferingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferingController.class);

    private final OfferingService offeringService;

    public OfferingController(OfferingService offeringService) {
        this.offeringService = offeringService;
    }

    // Testing only
    @GetMapping
    public ResponseEntity<Iterable<Offering>> getAllOfferings() {
        Iterable<Offering> offerings = offeringService.findAll();
        return ResponseEntity.ok(offerings);
    }

    @GetMapping("/{offeringId}")
    public ResponseEntity<Offering> getOffering(@PathVariable("offeringId") UUID offeringId) {
        Offering offering = offeringService.findById(offeringId);
        return ResponseEntity.ok(offering);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Offering>> getOfferingsByParams(@RequestParam("query") String query) {
        List<Offering> offerings = offeringService.findByQuery(query);
        return ResponseEntity.ok(offerings);
    }

    @PostMapping
    public ResponseEntity<Offering> createNewOffering(@RequestBody Offering offering,
                                                      HttpServletRequest httpServletRequest) {
        if (!authorizedUser(httpServletRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Offering createdOffering = offeringService.createOffering(offering);
        return ResponseEntity.ok(createdOffering);
    }

    @PostMapping("/{offeringId}/changeStatus")
    public ResponseEntity<Offering> changeOfferingStatus(@PathVariable("offeringId") UUID offeringId,
                                                         @RequestBody ChangeStatusRequest changeStatusRequest,
                                                         HttpServletRequest httpServletRequest) {
        if (!authorizedUser(httpServletRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Offering offering = offeringService.changeOfferingStatus(offeringId, changeStatusRequest.getStatus());

        if (offering == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(offering);
    }

    @PutMapping("/{offeringId}")
    public ResponseEntity<Offering> updateOffering(@PathVariable("offeringId") UUID offeringId,
                                                   @RequestBody Offering offering,
                                                   HttpServletRequest httpServletRequest) {
        if (!authorizedUser(httpServletRequest)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Offering updatedOffering = offeringService.updateOffering(offeringId, offering);

        if (updatedOffering == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedOffering);
    }

    private boolean authorizedUser(HttpServletRequest httpServletRequest) {
        String userIdFromHeader = httpServletRequest.getHeader("X-UserId");
        String role = httpServletRequest.getHeader("X-Role");

        LOGGER.debug("userIdFromHeader:{}", userIdFromHeader);
        LOGGER.debug("role:{}", role);

        return role.equals("Admin");
    }
}
