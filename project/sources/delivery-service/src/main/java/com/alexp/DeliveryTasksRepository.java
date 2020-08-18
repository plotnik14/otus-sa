package com.alexp;

import com.alexp.model.DeliveryTask;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryTasksRepository extends CrudRepository<DeliveryTask, UUID> {
    List<DeliveryTask> findAllByCourierId(UUID courierId);
}
