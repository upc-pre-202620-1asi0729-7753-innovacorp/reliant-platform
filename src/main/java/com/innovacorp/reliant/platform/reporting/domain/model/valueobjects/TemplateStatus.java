package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

import java.util.Arrays;

public enum TemplateStatus {
    ACTIVE, ARCHIVED;

    public static TemplateStatus fromString(String value) {
        try {
            return TemplateStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Status must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }
}
