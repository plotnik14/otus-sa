package com.alexp.model;

public enum OrderStatus {
    IN_PROGRESS("In Progress");

    private String name;

    OrderStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
