package com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects;

public record HvofSystemId(Long hvofSystemId) {
    public HvofSystemId {
        if (hvofSystemId == null || hvofSystemId <= 0) {
            throw new IllegalArgumentException("HVOF system id must be a positive number");
        }
    }
}