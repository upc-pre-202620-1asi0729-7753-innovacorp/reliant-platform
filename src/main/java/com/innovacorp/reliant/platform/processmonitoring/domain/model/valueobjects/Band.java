package com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects;

import java.util.Arrays;

/**
 * Classification band of a reading (process monitoring copy — no domain types are shared across contexts).
 */
public enum Band {
    NOMINAL, OUT_OF_NOMINAL, WARNING, SHUTDOWN;

    public static Band fromString(String value) {
        try {
            return Band.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Band must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }

    public boolean isDeviation() {
        return this == WARNING || this == SHUTDOWN;
    }
}
