package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;


/**
 * Operational status of an HVOF system.
 */
public enum HvofSystemStatus {
    ACTIVE, MAINTENANCE, OUT_OF_SERVICE;

    public static HvofSystemStatus fromString(String value) {
        try {
            return HvofSystemStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.hvof-system-status.invalid");
        }
    }
}