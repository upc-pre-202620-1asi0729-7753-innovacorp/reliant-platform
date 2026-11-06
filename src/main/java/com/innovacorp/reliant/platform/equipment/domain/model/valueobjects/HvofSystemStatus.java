package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

import java.util.Arrays;

/**
 * Operational status of an HVOF system.
 */
public enum HvofSystemStatus {
    ACTIVE, MAINTENANCE, OUT_OF_SERVICE;

    public static HvofSystemStatus fromString(String value) {
        try {
            return HvofSystemStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Status must be one of " + Arrays.toString(values()));
        }
    }
}