package com.alexp.adapter;

import com.alexp.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class OrderManagementAdapter {
    private static final String ORDER_MANAGEMENT_URL = "order-management:9000";

    private final RestTemplate restTemplate;

    public OrderManagementAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Order getOrderById(UUID orderId) {
        Order order = restTemplate.getForObject(
                "http://"+ ORDER_MANAGEMENT_URL +"/api/v1/orders/"+orderId,
                Order.class
        );
        return order;
    }
}
