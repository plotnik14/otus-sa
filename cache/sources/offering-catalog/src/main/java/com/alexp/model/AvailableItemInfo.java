package com.alexp.model;

public class AvailableItemInfo {
    private String address;
    private Long count;

    public AvailableItemInfo() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AvailableItemInfo{" +
                "address='" + address + '\'' +
                ", count=" + count +
                '}';
    }
}
