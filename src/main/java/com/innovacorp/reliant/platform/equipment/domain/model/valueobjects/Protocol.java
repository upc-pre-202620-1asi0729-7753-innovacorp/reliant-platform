package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;


public enum Protocol {
    ETHERNET_IP, OPC_UA, MODBUS_TCP, S7;

    public static Protocol fromString(String value) {
        try {
            return Protocol.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("validation.protocol.invalid");
        }
    }
}