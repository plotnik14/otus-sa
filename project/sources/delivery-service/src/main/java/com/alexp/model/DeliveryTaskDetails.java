package com.alexp.model;

import java.util.UUID;

public class DeliveryTaskDetails {
    private UUID taskId;
    private UUID courierId;
    private UUID orderId;
    private String status;
    private String comment;
    private Order order;
    private User customer;

    public DeliveryTaskDetails() {
    }

    public DeliveryTaskDetails(DeliveryTask deliveryTask) {
        taskId = deliveryTask.getTaskId();
        courierId = deliveryTask.getCourierId();
        orderId = deliveryTask.getOrderId();
        status = deliveryTask.getStatus();
        comment = deliveryTask.getComment();
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public UUID getCourierId() {
        return courierId;
    }

    public void setCourierId(UUID courierId) {
        this.courierId = courierId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "DeliveryTaskDetails{" +
                "taskId=" + taskId +
                ", courierId=" + courierId +
                ", orderId=" + orderId +
                ", status='" + status + '\'' +
                ", comment='" + comment + '\'' +
                ", order=" + order +
                ", customer=" + customer +
                '}';
    }
}
