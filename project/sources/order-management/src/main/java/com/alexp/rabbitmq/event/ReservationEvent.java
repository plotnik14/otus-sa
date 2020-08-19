package com.alexp.rabbitmq.event;

import java.io.Serializable;
import java.util.UUID;

public class ReservationEvent implements Serializable {
    private UUID orderId;
    private UUID customerId;
    private UUID idempotenceKey;

    public ReservationEvent() {
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

    public UUID getIdempotenceKey() {
        return idempotenceKey;
    }

    public void setIdempotenceKey(UUID idempotenceKey) {
        this.idempotenceKey = idempotenceKey;
    }

    @Override
    public String toString() {
        return "ReservationEvent{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", idempotenceKey=" + idempotenceKey +
                '}';
    }
}
