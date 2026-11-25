package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;


public enum TemplateStatus {
    ACTIVE, ARCHIVED;

    public static TemplateStatus fromString(String value) {
        try {
            return TemplateStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.template-status.invalid");
        }
    }
}