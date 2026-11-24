package com.innovacorp.reliant.platform.traceability.domain.model.valueobjects;

public record WorkOrderNumber(String workOrderNumber) {
    public WorkOrderNumber {
        if (workOrderNumber == null || !workOrderNumber.matches("^[A-Z0-9-]{4,30}$")) {
            throw new IllegalArgumentException("validation.work-order-number.format");
        }
    }
}