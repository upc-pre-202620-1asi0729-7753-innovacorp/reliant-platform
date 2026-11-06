package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

/**
 * HvofSystemCode Value Object — short identifier of a system inside its organization (e.g. HVOF-01).
 */
public record HvofSystemCode(String code) {
    public HvofSystemCode {
        if (code == null || !code.matches("^[A-Z0-9-]{3,20}$")) {
            throw new IllegalArgumentException("HVOF system code must be 3 to 20 upper case letters, digits or hyphens");
        }
    }
}