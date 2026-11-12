package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.commands.RegisterControllerCommand;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.RegisterControllerResource;

import java.util.List;

public class RegisterControllerCommandFromResourceAssembler {
    public static RegisterControllerCommand toCommandFromResource(Long hvofSystemId, RegisterControllerResource resource) {
        var protocols = resource.supportedProtocols() == null
                ? List.<String>of()
                : resource.supportedProtocols().stream().map(p -> p.protocol()).toList();

        return new RegisterControllerCommand(
                hvofSystemId,
                resource.controllerNumber(),
                resource.deviceType(),
                resource.serialNumber(),
                resource.manufacturer(),
                resource.model(),
                resource.partNumber(),
                resource.ipAddress(),
                resource.port(),
                resource.rack(),
                resource.slot(),
                resource.endpointUrl(),
                resource.tagCatalogId(),
                protocols
        );
    }
}