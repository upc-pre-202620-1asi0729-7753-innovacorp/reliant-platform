package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects;

import java.util.Arrays;

/** Only deviations open fault cases. */
public enum FaultBand {
    WARNING, SHUTDOWN;

    public static FaultBand fromString(String value) {
        try {
            return FaultBand.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Band must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }
}
