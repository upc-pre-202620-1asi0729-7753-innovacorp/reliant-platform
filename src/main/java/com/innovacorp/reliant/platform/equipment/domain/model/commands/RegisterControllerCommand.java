package com.innovacorp.reliant.platform.equipment.domain.model.commands;

import java.util.List;

public record RegisterControllerCommand(
        Long hvofSystemId,
        int controllerNumber,
        String deviceType,
        String serialNumber,
        String manufacturer,
        String model,
        String partNumber,
        String ipAddress,
        int port,
        int rack,
        int slot,
        String endpointUrl,
        String tagCatalogId,
        List<String> supportedProtocols
) {
}