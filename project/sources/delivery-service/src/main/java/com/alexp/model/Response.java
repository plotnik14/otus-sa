package com.alexp.model;

public class Response {
    private String status;

    public Response(String status) {
        this.status = status;
    }

    public Response() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
