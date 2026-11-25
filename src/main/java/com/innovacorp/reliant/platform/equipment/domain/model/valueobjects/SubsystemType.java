package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;


public enum SubsystemType {
    GAS_CONSOLE, POWDER_FEEDER, COOLING_UNIT, MANIPULATOR, DUST_COLLECTOR, SPRAY_GUN;

    public static SubsystemType fromString(String value) {
        try {
            return SubsystemType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.subsystem-type.invalid");
        }
    }
}