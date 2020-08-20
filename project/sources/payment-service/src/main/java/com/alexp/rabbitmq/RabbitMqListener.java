package com.alexp.rabbitmq;

import com.alexp.rabbitmq.event.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqListener.class);

    @RabbitListener(queues = "payment.ps.queue")
    public void receivePayment(PaymentEvent paymentEvent) {
        LOGGER.debug("Message:{}", paymentEvent);
    }
}
