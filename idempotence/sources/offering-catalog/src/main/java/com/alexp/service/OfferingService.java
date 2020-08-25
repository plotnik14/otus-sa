package com.alexp.service;

import com.alexp.adapter.WarehouseManagerAdapter;
import com.alexp.model.AvailabilityCheckResult;
import com.alexp.model.Offering;
import com.alexp.model.OfferingStatus;
import com.alexp.repository.OfferingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OfferingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferingService.class);

    private final OfferingRepository offeringRepository;
    private final WarehouseManagerAdapter warehouseManagerAdapter;

    public OfferingService(OfferingRepository offeringRepository, WarehouseManagerAdapter warehouseManagerAdapter) {
        this.offeringRepository = offeringRepository;
        this.warehouseManagerAdapter = warehouseManagerAdapter;
    }

    public Iterable<Offering> findAll() {
        Iterable<Offering> offerings = offeringRepository.findAll();
        return offerings;
    }

    @Cacheable(cacheNames="offerings", key = "#offeringId")
    public Offering findById(UUID offeringId) {
        LOGGER.debug("Data not from cache. Offering:{}", offeringId);
        Offering offering = offeringRepository.findById(offeringId).orElse(null);

        if (offering != null) {
            AvailabilityCheckResult availabilityCheckResult = warehouseManagerAdapter.checkOfferingAvailability(offeringId);
            LOGGER.debug("AvailabilityCheckResult:{}", availabilityCheckResult);

            if (availabilityCheckResult == null) {
                offering.setAvailable(0L);
            } else {
                offering.setAvailable(availabilityCheckResult.getTotalCount());
            }
        }

        return offering;
    }

    public List<Offering> findByQuery(String query) {
        List<Offering> offerings = offeringRepository.findAllByNameContains(query);

        offerings = offerings.stream()
                .filter(offering -> OfferingStatus.ACTIVE.getName().equals(offering.getStatus()))
                .collect(Collectors.toList());

        return offerings;
    }

    public Offering createOffering(Offering offering) {
        offering.setOfferingId(UUID.randomUUID());
        offering.setStatus(OfferingStatus.IN_DEVELOPMENT.getName());
        Offering createdOffering = offeringRepository.save(offering);
        return createdOffering;
    }

//    @CachePut(cacheNames="offerings", key = "#offeringId")
    @CacheEvict(cacheNames="offerings", key = "#offeringId")
    public Offering updateOffering(UUID offeringId, Offering offering) {
        LOGGER.debug("CacheEvict. offeringId:{}; offering:{}", offeringId, offering);
        Offering offeringToUpdate = offeringRepository.findById(offeringId).orElse(null);

        if (offeringToUpdate == null){
            return null;
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

        Offering updatedOfferings = offeringRepository.save(offeringToUpdate);
        LOGGER.debug("updatedOffering:{}", updatedOfferings);
        return updatedOfferings;
    }

//    @CachePut(cacheNames="offerings", key = "#offeringId")
    @CacheEvict(cacheNames="offerings", key = "#offeringId")
    public Offering changeOfferingStatus(UUID offeringId, String newStatus) {
        LOGGER.debug("CacheEvict. offeringId:{}", offeringId);
        Offering offering = offeringRepository.findById(offeringId).orElse(null);

        if (offering == null){
            return null;
        }

        // ToDo Transition validation

        offering.setStatus(newStatus);
        offering = offeringRepository.save(offering);
        LOGGER.debug("changeOfferingStatus#offering:{}", offering);
        return offering;
    }

}
