package com.alexp.model;

public enum OfferingStatus {
    IN_DEVELOPMENT("In Development"),
    ACTIVE("Active"),
    ARCHIVED("Archived");

    private String name;

    OfferingStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
