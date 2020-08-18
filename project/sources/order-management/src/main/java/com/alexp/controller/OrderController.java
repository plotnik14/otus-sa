package com.alexp.controller;

import com.alexp.model.*;
import com.alexp.rabbitmq.RabbitMessagingService;
import com.alexp.repository.OrderItemsRepository;
import com.alexp.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final RabbitMessagingService rabbitMessagingService;

    private long sequenceNumber; // Todo change to something better

    public OrderController(OrderRepository orderRepository,
                           OrderItemsRepository orderItemsRepository,
                           RabbitMessagingService rabbitMessagingService) {
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.rabbitMessagingService = rabbitMessagingService;
        initDB();
        sequenceNumber = 1;
    }

    @GetMapping
    public ResponseEntity<Iterable<Order>> getOrdersByParams(@RequestParam("customerId") UUID customerId) {
        Iterable<Order> orders = orderRepository.findAll();
        // ToDo BY CUSTOMER ID!

        rabbitMessagingService.sendOrder("ALPL Order");

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

        order.setStatus(changeStatusRequest.getStatus());
        order = orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<Order> addItemToOrder(@PathVariable("orderId") UUID orderId,
                                                @RequestBody OrderItem orderItem) {
        // ToDo add integration with catalog
        orderItem.setPrice(100.0);
        orderItem.setName("Offer Name 1");

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        // ToDo calculate price

        orderItem.setOrder(order);
        order.getItems().add(orderItem);

        orderItemsRepository.save(orderItem);

        order = orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Order> deleteItemFromOrder(@PathVariable("orderId") UUID orderId,
                                                     @PathVariable("itemId") UUID itemId) {


        orderItemsRepository.deleteById(itemId);

        // ToDo calculate price

        return ResponseEntity.noContent().build();
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

    private void initDB() {
        Order order = new Order();
        order.setName("Test Order");
        order.setCustomerId(UUID.randomUUID());
        order.setDeliveryAddress("Address");
        order.setPaymentMethod("Address");
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
