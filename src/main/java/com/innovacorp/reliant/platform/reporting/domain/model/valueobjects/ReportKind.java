package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;


/** What a template documents. Exposed in lowercase: quality_certificate, session_report, fault_summary. */
public enum ReportKind {
    QUALITY_CERTIFICATE, SESSION_REPORT, FAULT_SUMMARY;

    public static ReportKind fromString(String value) {
        try {
            return ReportKind.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.report-kind.invalid");
        }
    }
}