package com.alexp.rabbitmq.event;

import java.io.Serializable;
import java.util.UUID;

public class OrderStatusChangedEvent implements Serializable {
    private UUID orderId;
    private UUID customerId;
    private String orderName;
    private String previousStatus;
    private String newStatus;
    private UUID idempotenceKey;

    public OrderStatusChangedEvent() {
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public UUID getIdempotenceKey() {
        return idempotenceKey;
    }

    public void setIdempotenceKey(UUID idempotenceKey) {
        this.idempotenceKey = idempotenceKey;
    }

    @Override
    public String toString() {
        return "OrderStatusChanged{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", orderName='" + orderName + '\'' +
                ", previousStatus='" + previousStatus + '\'' +
                ", newStatus='" + newStatus + '\'' +
                ", idempotenceKey=" + idempotenceKey +
                '}';
    }
}
