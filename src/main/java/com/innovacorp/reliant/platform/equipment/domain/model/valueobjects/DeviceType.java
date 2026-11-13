package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

import java.util.Arrays;

public enum DeviceType {
    PLC, OPC_UA_SERVER, GATEWAY;

    public static DeviceType fromString(String value) {
        try {
            return DeviceType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Device type must be one of " + Arrays.toString(values()));
        }
    }
}