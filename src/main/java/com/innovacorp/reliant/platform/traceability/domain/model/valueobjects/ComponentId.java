package com.innovacorp.reliant.platform.traceability.domain.model.valueobjects;

public record ComponentId(Long componentId) {
    public ComponentId {
        if (componentId == null || componentId <= 0) {
            throw new IllegalArgumentException("Component id must be a positive number");
        }
    }
}
