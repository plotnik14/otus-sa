package com.alexp.rabbitmq;

import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitOrderListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitOrderListener.class);

    @RabbitListener(queues = "order.status.om.queue")
    public void receiveOrder(OrderStatusChangedEvent orderStatusChangedEvent) {
        LOGGER.debug("Message:{}", orderStatusChangedEvent);
    }
}
