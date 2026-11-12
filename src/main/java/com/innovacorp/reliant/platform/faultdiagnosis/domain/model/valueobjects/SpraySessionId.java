package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects;

public record SpraySessionId(Long spraySessionId) {
    public SpraySessionId {
        if (spraySessionId == null || spraySessionId <= 0) throw new IllegalArgumentException("Spray session id must be positive");
    }
}
