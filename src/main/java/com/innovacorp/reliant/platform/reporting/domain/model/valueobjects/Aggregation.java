package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;


public enum Aggregation {
    AVG, MIN, MAX, RANGE, LAST, COUNT;

    public static Aggregation fromString(String value) {
        try {
            return Aggregation.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.aggregation.invalid");
        }
    }
}