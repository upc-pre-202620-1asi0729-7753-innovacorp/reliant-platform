package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

import java.util.Arrays;
import java.util.stream.Collectors;

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
            String allowed = Arrays.stream(values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Invalid unit category. Allowed values: " + allowed);
        }
    }

    public String toLowerCase() {
        return name().toLowerCase();
    }
}
