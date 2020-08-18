package com.alexp.controller;

import com.alexp.model.AvailabilityCheckResult;
import com.alexp.model.AvailableItemInfo;
import com.alexp.model.WarehouseItem;
import com.alexp.repository.WarehouseItemsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/")
public class WarehouseController {

    private final WarehouseItemsRepository warehouseItemsRepository;

    public WarehouseController(WarehouseItemsRepository warehouseItemsRepository) {
        this.warehouseItemsRepository = warehouseItemsRepository;
        initDB();
    }

    @GetMapping("/availability")
    public ResponseEntity<AvailabilityCheckResult> getOfferingAvailability(@RequestParam("offeringId") UUID offeringId) {
        AvailabilityCheckResult availabilityCheckResult = new AvailabilityCheckResult();
        availabilityCheckResult.setOfferingId(offeringId);

        List<WarehouseItem> warehouseItems = warehouseItemsRepository.findAllByOfferingId(offeringId);


        Map<String, Long> countByAddressMap = new HashMap<>();

        for (WarehouseItem warehouseItem : warehouseItems) {
            String address = warehouseItem.getAddress();
            Long count = countByAddressMap.get(address);

            if (count == null) {
                count = 1L;
            } else {
                count++;
            }

            countByAddressMap.put(address, count);
        }

        long totalCount = 0;
        List<AvailableItemInfo> availability = new ArrayList<>();

        for (String address : countByAddressMap.keySet()) {
            AvailableItemInfo availableItemInfo = new AvailableItemInfo();
            availableItemInfo.setAddress(address);
            Long count = countByAddressMap.get(address);
            availableItemInfo.setCount(count);
            totalCount += count;
            availability.add(availableItemInfo);
        }

        availabilityCheckResult.setAvailability(availability);
        availabilityCheckResult.setTotalCount(totalCount);
        return ResponseEntity.ok(availabilityCheckResult);
    }

    public void initDB() {
        List<WarehouseItem> warehouseItems = new ArrayList<>();

        WarehouseItem warehouseItem = new WarehouseItem();
        warehouseItem.setAddress("Address 1");
        warehouseItem.setOfferingId(UUID.fromString("e0e7984c-e139-11ea-87d0-0242ac130003"));
        warehouseItems.add(warehouseItem);

        warehouseItem = new WarehouseItem();
        warehouseItem.setAddress("Address 1");
        warehouseItem.setOfferingId(UUID.fromString("e0e7984c-e139-11ea-87d0-0242ac130003"));
        warehouseItems.add(warehouseItem);

        warehouseItem = new WarehouseItem();
        warehouseItem.setAddress("Address 1");
        warehouseItem.setOfferingId(UUID.fromString("e0e7984c-e139-11ea-87d0-0242ac130003"));
        warehouseItems.add(warehouseItem);

        warehouseItem = new WarehouseItem();
        warehouseItem.setAddress("Address 2");
        warehouseItem.setOfferingId(UUID.fromString("e0e7984c-e139-11ea-87d0-0242ac130003"));
        warehouseItems.add(warehouseItem);


        warehouseItem = new WarehouseItem();
        warehouseItem.setAddress("Address 1");
        warehouseItem.setOfferingId(UUID.fromString("d05be6bc-e13a-11ea-87d0-0242ac130003"));
        warehouseItems.add(warehouseItem);

        warehouseItemsRepository.saveAll(warehouseItems);
    }

}
