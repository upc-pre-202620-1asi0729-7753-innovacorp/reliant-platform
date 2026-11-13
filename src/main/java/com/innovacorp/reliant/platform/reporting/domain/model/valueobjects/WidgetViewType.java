package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

import java.util.Arrays;

public enum WidgetViewType {
    VALUE, MIN_MAX, TREND, TABLE, TEXT;

    public static WidgetViewType fromString(String value) {
        try {
            return WidgetViewType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("View type must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }
}
