package com.alexp.model;

public enum OrderStatus {
    IN_PROGRESS("In Progress"),
    SUBMITTED("Submitted"),
    READY_FOR_PAYMENT("Ready for payment"),
    READY_FOR_DELIVERY("Ready for delivery"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
