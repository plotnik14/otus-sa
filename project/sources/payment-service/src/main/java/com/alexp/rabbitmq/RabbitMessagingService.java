package com.alexp.rabbitmq;

import com.alexp.rabbitmq.event.PaymentEvent;
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

    public void sendPaymentEvent(PaymentEvent paymentEvent) {
        paymentEvent.setIdempotenceKey(UUID.randomUUID());
        rabbitTemplate.convertAndSend("payment.exchange", "payment.*", paymentEvent);
        LOGGER.debug("Message sent:{}", paymentEvent);
    }
}
