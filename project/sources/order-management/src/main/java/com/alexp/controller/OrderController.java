package com.alexp.controller;

import com.alexp.adapter.OfferingCatalogAdapter;
import com.alexp.model.*;
import com.alexp.rabbitmq.RabbitMessagingService;
import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import com.alexp.repository.OrderItemsRepository;
import com.alexp.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final RabbitMessagingService rabbitMessagingService;
    private final OfferingCatalogAdapter offeringCatalogAdapter;

    private long sequenceNumber; // Todo change to something better

    public OrderController(OrderRepository orderRepository,
                           OrderItemsRepository orderItemsRepository,
                           RabbitMessagingService rabbitMessagingService,
                           OfferingCatalogAdapter offeringCatalogAdapter) {
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.rabbitMessagingService = rabbitMessagingService;
        this.offeringCatalogAdapter = offeringCatalogAdapter;
        initDB();
        sequenceNumber = 1;
    }

    @GetMapping
    public ResponseEntity<Iterable<Order>> getOrdersByParams(@RequestParam("customerId") UUID customerId) {
        Iterable<Order> orders = orderRepository.findAllByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable("orderId") UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        Order order = new Order();

        order.setName("Order#" + sequenceNumber++);
        order.setCustomerId(createOrderRequest.getCustomerId());
        order.setDeliveryAddress(createOrderRequest.getDeliveryAddress());
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());
        order.setStatus(OrderStatus.IN_PROGRESS.getName());

        order = orderRepository.save(order);

        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/changeStatus")
    public ResponseEntity<Order> changeOrderStatus(@PathVariable("orderId") UUID orderId,
                                             @RequestBody ChangeStatusRequest changeStatusRequest) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        //ToDo validation

        String previousStatus = order.getStatus();

        order.setStatus(changeStatusRequest.getStatus());
        order = orderRepository.save(order);

        OrderStatusChangedEvent orderStatusChangedEvent = new OrderStatusChangedEvent();
        orderStatusChangedEvent.setOrderId(order.getOrderId());
        orderStatusChangedEvent.setCustomerId(order.getCustomerId());
        orderStatusChangedEvent.setOrderName(order.getName());
        orderStatusChangedEvent.setPreviousStatus(previousStatus);
        orderStatusChangedEvent.setNewStatus(order.getStatus());

        rabbitMessagingService.sendOrderStatusChangedEvent(orderStatusChangedEvent);

        LOGGER.debug("Order:{} status changed from {} to {}", order.getOrderId(), previousStatus, order.getStatus());

        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<Order> addItemToOrder(@PathVariable("orderId") UUID orderId,
                                                @RequestBody OrderItem orderItem) {
        Offering offering = offeringCatalogAdapter.getOfferingById(orderItem.getOfferingId());

        LOGGER.debug("Found offering:{}", offering);

        if (offering == null) {
            return ResponseEntity.notFound().build();
        }

        orderItem.setPrice(offering.getPrice());
        orderItem.setName(offering.getName());

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        orderItem.setOrder(order);
        order.getItems().add(orderItem);

        orderItemsRepository.save(orderItem);

        calculateTotalPrice(order);

        order = orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Order> deleteItemFromOrder(@PathVariable("orderId") UUID orderId,
                                                     @PathVariable("itemId") UUID itemId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        orderItemsRepository.deleteById(itemId);

        calculateTotalPrice(order);

        order = orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Order> updateOrderParams(@PathVariable("orderId") UUID orderId,
                                                @RequestBody Order orderParams) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        if (orderParams.getName() != null) {
            order.setName(orderParams.getName());
        }

        if (orderParams.getPaymentMethod() != null) {
            order.setPaymentMethod(orderParams.getPaymentMethod());
        }

        if (orderParams.getDeliveryAddress() != null) {
            order.setDeliveryAddress(orderParams.getDeliveryAddress());
        }

        order = orderRepository.save(order);

        return ResponseEntity.ok(order);
    }

    private void calculateTotalPrice(Order order){
        double totalPrice = 0;

        for (OrderItem item : order.getItems()) {
            totalPrice += item.getPrice();
        }

        order.setTotalPrice(totalPrice);
    }

    private void initDB() {
        Order order = new Order();
        order.setName("Test Order");
        order.setCustomerId(UUID.fromString("d1fa0ebe-e247-11ea-87d0-0242ac130003"));
        order.setDeliveryAddress("Address");
        order.setPaymentMethod("Credit Card");
        order.setStatus("In Progress");
        order.setTotalPrice(20.0);

        OrderItem item = new OrderItem();
        item.setOfferingId(UUID.randomUUID());
        item.setPrice(20.0);
        item.setName("Test item");
        item.setOrder(order);

        orderRepository.save(order);
    }
}
