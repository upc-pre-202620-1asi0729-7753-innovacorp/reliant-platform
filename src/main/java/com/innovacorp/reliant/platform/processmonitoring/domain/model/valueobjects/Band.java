package com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects;


/**
 * Classification band of a reading (process monitoring copy — no domain types are shared across contexts).
 */
public enum Band {
    NOMINAL, OUT_OF_NOMINAL, WARNING, SHUTDOWN;

    public static Band fromString(String value) {
        try {
            return Band.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.band.invalid");
        }
    }

    public boolean isDeviation() {
        return this == WARNING || this == SHUTDOWN;
    }
}