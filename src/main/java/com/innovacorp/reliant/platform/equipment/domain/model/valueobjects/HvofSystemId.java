package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

public record HvofSystemId(Long hvofSystemId) {
    public HvofSystemId {
        if (hvofSystemId == null || hvofSystemId <= 0) {
            throw new IllegalArgumentException("validation.hvof-system-id.positive");
        }
    }
}