package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;


/** The record a generated report is about. */
public enum ReportSubjectType {
    SPRAY_SESSION, RECUPERATION;

    public static ReportSubjectType fromString(String value) {
        try {
            return ReportSubjectType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.report-subject-type.invalid");
        }
    }
}