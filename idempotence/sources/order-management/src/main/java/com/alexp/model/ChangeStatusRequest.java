package com.alexp.model;

public class ChangeStatusRequest {
    private String status;
    private Long orderVersion;

    public ChangeStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrderVersion() {
        return orderVersion;
    }

    public void setOrderVersion(Long orderVersion) {
        this.orderVersion = orderVersion;
    }
}
