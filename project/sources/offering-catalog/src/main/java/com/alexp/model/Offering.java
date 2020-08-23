package com.alexp.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Offering implements Serializable {
    @Id
//    @GeneratedValue
    private UUID offeringId;
    private String name;
    private Double price;
    private String description;
    private Long available;
    private String status;

    public Offering() {
        offeringId = UUID.randomUUID();
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

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offering offering = (Offering) o;
        return Objects.equals(offeringId, offering.offeringId) &&
                Objects.equals(name, offering.name) &&
                Objects.equals(price, offering.price) &&
                Objects.equals(description, offering.description) &&
                Objects.equals(available, offering.available) &&
                Objects.equals(status, offering.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offeringId, name, price, description, available, status);
    }

    @Override
    public String toString() {
        return "Offering{" +
                "offeringId=" + offeringId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", available=" + available +
                ", status='" + status + '\'' +
                '}';
    }
}
