package com.innovacorp.reliant.platform.traceability.domain.model.valueobjects;


public enum ComponentType {
    HYDRAULIC_ROD, CYLINDER_BLOCK, SHAFT, IMPELLER, OTHER;

    public static ComponentType fromString(String value) {
        try {
            return ComponentType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.component-type.invalid");
        }
    }
}