package com.alexp.rabbitmq;

import com.alexp.controller.OrderController;
import com.alexp.model.ChangeStatusRequest;
import com.alexp.model.Order;
import com.alexp.model.OrderStatus;
import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import com.alexp.rabbitmq.event.PaymentEvent;
import com.alexp.rabbitmq.event.ReservationEvent;
import com.alexp.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EnableRabbit
@Component
public class RabbitMqListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqListener.class);

    //ToDo refactoring to Service
    private final OrderController orderController;

    private final OrderRepository orderRepository;

    private final Set<UUID> processedRequestIds;

    public RabbitMqListener(OrderController orderController,
                            OrderRepository orderRepository) {
        this.orderController = orderController;
        this.orderRepository = orderRepository;

        processedRequestIds = new HashSet<>();
    }

    @RabbitListener(queues = "order.status.om.queue")
    public void receiveOrder(OrderStatusChangedEvent orderStatusChangedEvent) {
        LOGGER.debug("Message:{}", orderStatusChangedEvent);
    }

    @RabbitListener(queues = "order.reservation.om.queue")
    public void receiveReservation(ReservationEvent reservationEvent) {
        LOGGER.debug("Message:{}", reservationEvent);

        if (processedRequestIds.contains(reservationEvent.getIdempotenceKey())){
            LOGGER.info("Event with key:{} was already processed", reservationEvent.getIdempotenceKey());
        } else {
            processedRequestIds.add(reservationEvent.getIdempotenceKey());
        }

        Order order = orderRepository.findById(reservationEvent.getOrderId()).orElse(null);
        if (order == null) {
            LOGGER.error("Invalid event. Order is null:{}", reservationEvent);
            return;
        }

        ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
        changeStatusRequest.setStatus(OrderStatus.READY_FOR_PAYMENT.getName());
        orderController.changeOrderStatus(reservationEvent.getOrderId(), order.getVersion(), changeStatusRequest);
    }

    @RabbitListener(queues = "payment.om.queue")
    public void receivePayment(PaymentEvent paymentEvent) {
        LOGGER.debug("Message:{}", paymentEvent);

        if (processedRequestIds.contains(paymentEvent.getIdempotenceKey())){
            LOGGER.info("Event with key:{} was already processed", paymentEvent.getIdempotenceKey());
        } else {
            processedRequestIds.add(paymentEvent.getIdempotenceKey());
        }

        Order order = orderRepository.findById(paymentEvent.getOrderId()).orElse(null);
        if (order == null) {
            LOGGER.error("Invalid event. Order is null:{}", paymentEvent);
            return;
        }

        ChangeStatusRequest changeStatusRequest = new ChangeStatusRequest();
        changeStatusRequest.setStatus(OrderStatus.READY_FOR_DELIVERY.getName());
        orderController.changeOrderStatus(paymentEvent.getOrderId(), order.getVersion(), changeStatusRequest);
    }
}
