package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

@Schema(name = "RegisterControllerRequest",
        example = "{\"controllerNumber\": 1, \"deviceType\": \"PLC\", \"serialNumber\": \"CLX-5580-001\", \"manufacturer\": \"Rockwell\", \"model\": \"1756-L83E\", \"partNumber\": \"1756-L83E\", \"ipAddress\": \"192.168.10.21\", \"port\": 44818, \"rack\": 0, \"slot\": 0, \"endpointUrl\": null, \"tagCatalogId\": null, \"supportedProtocols\": [{\"protocol\": \"ETHERNET_IP\"}]}")
public record RegisterControllerResource(
        @Min(value = 1, message = "{validation.positive}")
        @Max(99)
        @Schema(example = "1")
        int controllerNumber,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "PLC", allowableValues = {"PLC", "OPC_UA_SERVER", "GATEWAY"})
        String deviceType,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "CLX-5580-001")
        String serialNumber,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "Rockwell")
        String manufacturer,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "1756-L83E")
        String model,

        @Schema(example = "1756-L83E")
        String partNumber,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "192.168.10.21")
        String ipAddress,

        @Min(value = 1, message = "{validation.positive}")
        @Max(65535)
        @Schema(example = "44818")
        int port,

        @Min(0)
        @Schema(example = "0")
        int rack,

        @Min(0)
        @Schema(example = "0")
        int slot,

        @Schema(example = "opc.tcp://192.168.10.21:4840")
        String endpointUrl,

        @Schema(example = "hvof-01-tags-v3")
        String tagCatalogId,

        List<@Valid ProtocolResource> supportedProtocols
) {
}