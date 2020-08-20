package com.alexp.rabbitmq;

import com.alexp.DeliveryTasksRepository;
import com.alexp.adapter.UserManagementAdapter;
import com.alexp.model.DeliveryTask;
import com.alexp.model.OrderStatus;
import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@EnableRabbit
@Component
public class RabbitMqListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqListener.class);

    private final UserManagementAdapter userManagementAdapter;
    private final DeliveryTasksRepository deliveryTasksRepository;

    //ToDo update
    private final UUID ALONE_COURIER_UUID = UUID.fromString("646d54b0-e142-11ea-87d0-0242ac130003");

    public RabbitMqListener(UserManagementAdapter userManagementAdapter, DeliveryTasksRepository deliveryTasksRepository) {
        this.userManagementAdapter = userManagementAdapter;
        this.deliveryTasksRepository = deliveryTasksRepository;
    }

    @RabbitListener(queues = "order.status.ds.queue")
    public void receiveOrder(OrderStatusChangedEvent orderStatusChangedEvent) {
        LOGGER.debug("Message:{}", orderStatusChangedEvent);
        if (OrderStatus.READY_FOR_DELIVERY.getName().equals(orderStatusChangedEvent.getNewStatus())) {
            DeliveryTask deliveryTask = new DeliveryTask();
            deliveryTask.setCourierId(ALONE_COURIER_UUID);
            deliveryTask.setOrderId(orderStatusChangedEvent.getOrderId());
            deliveryTask.setStatus("Open");
            deliveryTask.setComment("Comment:" + orderStatusChangedEvent.getOrderName());
            deliveryTasksRepository.save(deliveryTask);
        }
    }
}
