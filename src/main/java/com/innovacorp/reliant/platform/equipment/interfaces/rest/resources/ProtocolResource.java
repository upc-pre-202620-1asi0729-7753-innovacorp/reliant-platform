package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "SupportedProtocol", example = "{\"protocol\": \"ETHERNET_IP\"}")
public record ProtocolResource(
        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "ETHERNET_IP", allowableValues = {"ETHERNET_IP", "OPC_UA", "MODBUS_TCP", "S7"})
        String protocol
) {
}