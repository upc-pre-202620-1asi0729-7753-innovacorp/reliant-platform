package com.innovacorp.reliant.platform.reporting.domain.model.valueobjects;


/** Where a widget takes its data from: process readings, the session, the recuperation order or the fault cases. */
public enum VariableSource {
    PROCESS, SESSION, RECUPERATION, FAULT_CASES;

    public static VariableSource fromString(String value) {
        try {
            return VariableSource.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.variable-source.invalid");
        }
    }
}