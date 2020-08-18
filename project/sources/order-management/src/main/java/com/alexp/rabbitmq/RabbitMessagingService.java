package com.alexp.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMessagingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMessagingService.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(String order) {
        LOGGER.debug("Message to send:{}", order);

        rabbitTemplate.convertAndSend("order.status.exchange", "order.status.changed", order);
    }
}
