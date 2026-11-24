package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

public enum UnitCategory {
    FLOW,
    MASS_FLOW,
    PRESSURE,
    TEMPERATURE,
    SPEED,
    DIMENSIONLESS;

    public static UnitCategory fromString(String value) {
        try {
            return UnitCategory.valueOf(value.trim().toUpperCase());
        } catch (Exception exception) {
            throw new IllegalArgumentException("validation.unit-category.invalid");
        }
    }

    public String toLowerCase() {
        return name().toLowerCase();
    }
}