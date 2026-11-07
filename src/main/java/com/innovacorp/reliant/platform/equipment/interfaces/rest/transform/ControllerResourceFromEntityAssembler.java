package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.entities.Controller;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.ControllerResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.ProtocolResource;

public class ControllerResourceFromEntityAssembler {
    public static ControllerResource toResourceFromEntity(Controller entity, Long hvofSystemId) {
        var ep = entity.getEndpoint();

        return new ControllerResource(
                entity.getId(),
                hvofSystemId,
                entity.getControllerNumber(),
                entity.getDeviceType().name(),
                entity.getSerialNumber(),
                entity.getManufacturer(),
                entity.getModel(),
                entity.getPartNumber(),
                ep.ipAddress(),
                ep.port(),
                ep.rack(),
                ep.slot(),
                entity.getEndpointUrl(),
                entity.getTagCatalogId(),
                entity.getSupportedProtocols().stream().map(p -> new ProtocolResource(p.name())).toList()
        );
    }
}