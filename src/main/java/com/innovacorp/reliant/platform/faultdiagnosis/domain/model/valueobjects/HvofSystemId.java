package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects;

public record HvofSystemId(Long hvofSystemId) {
    public HvofSystemId {
        if (hvofSystemId == null || hvofSystemId <= 0) throw new IllegalArgumentException("HVOF system id must be positive");
    }
}
