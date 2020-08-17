package com.alexp.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Offering {
    @Id
    @GeneratedValue
    private UUID offeringId;
    private String name;
    private Double price;
    private String description;
    private Integer available;
    private String status;

    public Offering() {
    }

    public UUID getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(UUID offeringId) {
        this.offeringId = offeringId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
