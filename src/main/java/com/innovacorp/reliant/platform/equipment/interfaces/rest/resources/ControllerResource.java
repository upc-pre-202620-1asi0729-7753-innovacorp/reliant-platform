package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ControllerResponse")
public record ControllerResource(
        Long id,
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
        List<ProtocolResource> supportedProtocols
) {
}