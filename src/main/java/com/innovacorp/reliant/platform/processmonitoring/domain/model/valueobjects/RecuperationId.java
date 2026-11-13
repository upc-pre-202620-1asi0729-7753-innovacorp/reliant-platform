package com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects;

public record RecuperationId(Long recuperationId) {
    public RecuperationId {
        if (recuperationId == null || recuperationId <= 0) throw new IllegalArgumentException("Recuperation id must be a positive number");
    }
}
