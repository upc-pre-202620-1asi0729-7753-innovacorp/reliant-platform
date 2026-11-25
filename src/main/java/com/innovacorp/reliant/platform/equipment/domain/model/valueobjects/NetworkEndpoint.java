package com.innovacorp.reliant.platform.equipment.domain.model.valueobjects;

/**
 * NetworkEndpoint Value Object — IPv4 address, port and backplane rack/slot of a controller.
 */
public record NetworkEndpoint(String ipAddress, int port, int rack, int slot) {
    public NetworkEndpoint {
        if (ipAddress == null || !ipAddress.matches("^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$")) {
            throw new IllegalArgumentException("validation.network-endpoint.ip-address.invalid");
        }
        if (port < 1 || port > 65535) throw new IllegalArgumentException("validation.network-endpoint.port.range");
        if (rack < 0 || slot < 0) throw new IllegalArgumentException("validation.network-endpoint.rack-slot.negative");
    }
}