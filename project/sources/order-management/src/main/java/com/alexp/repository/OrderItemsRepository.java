package com.alexp.repository;

import com.alexp.model.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface OrderItemsRepository extends CrudRepository<OrderItem, UUID> {

}
