package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;

import java.util.Arrays;

/** The record a generated report is about. */
public enum ReportSubjectType {
    SPRAY_SESSION, RECUPERATION;

    public static ReportSubjectType fromString(String value) {
        try {
            return ReportSubjectType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Subject type must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }
}
