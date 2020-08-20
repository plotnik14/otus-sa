package com.alexp.rabbitmq;

import com.alexp.controller.OrderController;
import com.alexp.model.ChangeStatusRequest;
import com.alexp.model.OrderStatus;
import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import com.alexp.rabbitmq.event.PaymentEvent;
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

    //ToDo refactoring to Service
    private final OrderController orderController;

    public RabbitMqListener(OrderController orderController) {
        this.orderController = orderController;
    }

    @RabbitListener(queues = "order.status.om.queue")
    public void receiveOrder(OrderStatusChangedEvent orderStatusChangedEvent) {
        LOGGER.debug("Message:{}", orderStatusChangedEvent);
    }

    @RabbitListener(queues = "order.reservation.om.queue")
    public void receiveReservation(ReservationEvent reservationEvent) {
        LOGGER.debug("Message:{}", reservationEvent);
        ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
        changeStatusRequest.setStatus(OrderStatus.READY_FOR_PAYMENT.getName());
        orderController.changeOrderStatus(reservationEvent.getOrderId(),changeStatusRequest);
    }

    @RabbitListener(queues = "payment.om.queue")
    public void receivePayment(PaymentEvent paymentEvent) {
        LOGGER.debug("Message:{}", paymentEvent);
        ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
        changeStatusRequest.setStatus(OrderStatus.READY_FOR_DELIVERY.getName());
        orderController.changeOrderStatus(paymentEvent.getOrderId(), changeStatusRequest);
    }
}
