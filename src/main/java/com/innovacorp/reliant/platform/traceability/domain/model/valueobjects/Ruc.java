package com.innovacorp.reliant.platform.traceability.domain.model.valueobjects;

/**
 * RUC value object for the Traceability bounded context.
 */
public record Ruc(String ruc) {
    public Ruc {
        if (ruc == null || !ruc.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("RUC must be exactly 11 digits");
        }
    }
}
}