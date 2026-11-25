package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;


/**
 * Fuel used by the HVOF gun.
 */
public enum FuelType {
    HYDROGEN, KEROSENE, NATURAL_GAS, PROPANE;

    public static FuelType fromString(String value) {
        try {
            return FuelType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.fuel-type.invalid");
        }
    }
}