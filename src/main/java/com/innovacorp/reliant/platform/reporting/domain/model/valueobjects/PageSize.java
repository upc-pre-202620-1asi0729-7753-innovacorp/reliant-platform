package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

import java.util.Arrays;

public enum PageSize {
    A4, LETTER;

    public static PageSize fromString(String value) {
        try {
            return PageSize.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Page size must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }
}
