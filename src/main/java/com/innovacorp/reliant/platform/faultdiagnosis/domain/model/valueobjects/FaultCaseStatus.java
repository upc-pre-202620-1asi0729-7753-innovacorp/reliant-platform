package com.innovacorp.reliant.platform.faultdiagnosis.domain.model.valueobjects;

import java.util.Arrays;

public enum FaultCaseStatus {
    OPEN, ACKNOWLEDGED, RESOLVED;

    public static FaultCaseStatus fromString(String value) {
        try {
            return FaultCaseStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Status must be one of " + Arrays.toString(values()).toLowerCase());
        }
    }
}
