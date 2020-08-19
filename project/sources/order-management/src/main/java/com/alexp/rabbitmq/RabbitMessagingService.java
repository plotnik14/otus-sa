package com.alexp.rabbitmq;

import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitMessagingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMessagingService.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderStatusChangedEvent(OrderStatusChangedEvent orderStatusChangedEvent) {
        orderStatusChangedEvent.setIdempotenceKey(UUID.randomUUID());
        rabbitTemplate.convertAndSend("order.status.exchange", "order.status.*", orderStatusChangedEvent);
        LOGGER.debug("Message sent:{}", orderStatusChangedEvent);
    }
}
