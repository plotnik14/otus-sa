package com.alexp.repository;

import com.alexp.model.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {
    List<Order> findAllByCustomerId(UUID customerId);
}
