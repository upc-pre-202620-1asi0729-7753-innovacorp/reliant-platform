package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

import java.util.Arrays;

public enum WidgetWidth {
    HALF, FULL;

    public static WidgetWidth fromString(String value) {
        try {
            return WidgetWidth.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Width must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }
}
