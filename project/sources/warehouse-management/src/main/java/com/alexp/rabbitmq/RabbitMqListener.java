package com.alexp.rabbitmq;

import com.alexp.model.OrderStatus;
import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import com.alexp.rabbitmq.event.ReservationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqListener.class);

    private final RabbitMessagingService rabbitMessagingService;

    public RabbitMqListener(RabbitMessagingService rabbitMessagingService) {
        this.rabbitMessagingService = rabbitMessagingService;
    }

    @RabbitListener(queues = "order.status.whm.queue")
    public void receiveOrder(OrderStatusChangedEvent orderStatusChangedEvent) {
        LOGGER.debug("Message:{}", orderStatusChangedEvent);

        if(OrderStatus.SUBMITTED.getName().equals(orderStatusChangedEvent.getNewStatus())){

            //ToDo reservation

            ReservationEvent reservationEvent = new ReservationEvent();
            reservationEvent.setOrderId(orderStatusChangedEvent.getOrderId());
            reservationEvent.setCustomerId(orderStatusChangedEvent.getCustomerId());

            rabbitMessagingService.sendReservationEvent(reservationEvent);
        }
    }

    @RabbitListener(queues = "order.reservation.whm.queue")
    public void receiveReservation(ReservationEvent reservationEvent) {
        LOGGER.debug("Message:{}", reservationEvent);
    }
}
