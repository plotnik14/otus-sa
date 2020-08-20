package com.alexp.rabbitmq.event;

import java.io.Serializable;
import java.util.UUID;

public class PaymentEvent implements Serializable {
    private UUID orderId;
//    private UUID customerId;
    private UUID idempotenceKey;

    public PaymentEvent() {
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }


    public UUID getIdempotenceKey() {
        return idempotenceKey;
    }

    public void setIdempotenceKey(UUID idempotenceKey) {
        this.idempotenceKey = idempotenceKey;
    }

    @Override
    public String toString() {
        return "PaymentEvent{" +
                "orderId=" + orderId +
                ", idempotenceKey=" + idempotenceKey +
                '}';
    }
}