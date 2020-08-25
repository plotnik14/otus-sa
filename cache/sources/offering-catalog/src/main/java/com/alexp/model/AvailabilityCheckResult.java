package com.alexp.model;

import java.util.List;
import java.util.UUID;

public class AvailabilityCheckResult {
    private UUID offeringId;
    private Long totalCount;
    private List<AvailableItemInfo> availability;

    public AvailabilityCheckResult() {
    }

    public UUID getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(UUID offeringId) {
        this.offeringId = offeringId;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<AvailableItemInfo> getAvailability() {
        return availability;
    }

    public void setAvailability(List<AvailableItemInfo> availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "AvailabilityCheckResult{" +
                "offeringId=" + offeringId +
                ", totalCount=" + totalCount +
                ", availability=" + availability +
                '}';
    }
}
