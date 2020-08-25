package com.alexp.repository;

import com.alexp.model.Offering;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OfferingRepository extends CrudRepository<Offering, UUID> {
    List<Offering> findAllByNameContains(String query);
}
