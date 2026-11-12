package com.innovacorp.reliant.platform.traceability.domain.model.valueobjects;

/**
 * Ruc Value Object (traceability copy — bounded contexts do not share domain types).
 */
public record Ruc(String ruc) {
    public Ruc {
        if (ruc == null || !ruc.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("RUC must be exactly 11 digits");
        }
    }
}
