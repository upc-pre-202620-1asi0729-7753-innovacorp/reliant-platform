package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects;


/** Only deviations open fault cases. */
public enum FaultBand {
    WARNING, SHUTDOWN;

    public static FaultBand fromString(String value) {
        try {
            return FaultBand.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.fault-band.invalid");
        }
    }
}