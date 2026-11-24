package com.innovacorp.reliant.platform.traceability.domain.model.valueobjects;

public record CustomerId(Long customerId) {
    public CustomerId {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("validation.customer-id.positive");
        }
    }
}