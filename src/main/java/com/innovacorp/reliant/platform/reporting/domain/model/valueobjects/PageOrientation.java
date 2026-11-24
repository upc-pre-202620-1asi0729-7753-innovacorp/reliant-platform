package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;


public enum PageOrientation {
    PORTRAIT, LANDSCAPE;

    public static PageOrientation fromString(String value) {
        try {
            return PageOrientation.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.page-orientation.invalid");
        }
    }
}