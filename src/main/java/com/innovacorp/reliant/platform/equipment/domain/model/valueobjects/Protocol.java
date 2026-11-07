package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

import java.util.Arrays;

public enum Protocol {
    ETHERNET_IP, OPC_UA, MODBUS_TCP, S7;

    public static Protocol fromString(String value) {
        try {
            return Protocol.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Protocol must be one of " + Arrays.toString(values()));
        }
    }
}