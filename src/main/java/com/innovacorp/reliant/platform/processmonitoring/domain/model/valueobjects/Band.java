package com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects;

import java.util.Arrays;
import java.util.Locale;

public enum Band {
    NOMINAL, OUT_OF_NOMINAL, WARNING, SHUTDOWN;

    public static Band fromString(String value) {
        try {
            return Band.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(
                    "Band must be one of " + Arrays.toString(values()).toLowerCase(Locale.ROOT));
        }
    }

    public boolean isDeviation() {
        return this == WARNING || this == SHUTDOWN;
    }
}