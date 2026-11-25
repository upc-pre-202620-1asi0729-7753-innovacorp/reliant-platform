package com.innovacorp.reliant.platform.iam.domain.model.valueobjects;

/**
 * Ruc Value Object — Peruvian tax id, 11 digits.
 */
public record Ruc(String ruc) {
    public Ruc {
        if (ruc == null || !ruc.matches("^\\d{11}$")) {
            throw new IllegalArgumentException("validation.ruc.format");
        }
    }
}