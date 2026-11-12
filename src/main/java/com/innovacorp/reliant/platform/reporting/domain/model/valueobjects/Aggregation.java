package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

import java.util.Arrays;

public enum Aggregation {
    AVG, MIN, MAX, RANGE, LAST, COUNT;

    public static Aggregation fromString(String value) {
        try {
            return Aggregation.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Aggregation must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }
}
