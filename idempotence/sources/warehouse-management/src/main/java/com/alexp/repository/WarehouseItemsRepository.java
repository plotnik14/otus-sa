package com.alexp.repository;

import com.alexp.model.WarehouseItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface WarehouseItemsRepository extends CrudRepository<WarehouseItem, UUID> {
    List<WarehouseItem> findAllByOfferingId(UUID offeringId);
}
