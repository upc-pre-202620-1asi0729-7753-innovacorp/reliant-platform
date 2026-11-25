package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;


public enum WidgetWidth {
    HALF, FULL;

    public static WidgetWidth fromString(String value) {
        try {
            return WidgetWidth.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.widget-width.invalid");
        }
    }
}