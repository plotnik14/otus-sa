package com.alexp.rabbitmq;

import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import com.alexp.rabbitmq.event.ReservationEvent;
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

    public void sendReservationEvent(ReservationEvent reservationEvent) {
        reservationEvent.setIdempotenceKey(UUID.randomUUID());
        rabbitTemplate.convertAndSend("order.reservation.exchange", "order.reservation.*", reservationEvent);
        LOGGER.debug("Message sent:{}", reservationEvent);
    }
}
