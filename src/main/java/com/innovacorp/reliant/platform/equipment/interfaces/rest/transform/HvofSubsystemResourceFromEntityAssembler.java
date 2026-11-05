package com.innovacorp.reliant.platform.equipment.interfaces.rest.transform;

import com.innovacorp.reliant.platform.equipment.domain.model.entities.HvofSubsystem;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.HvofSubsystemResource;
import com.innovacorp.reliant.platform.equipment.interfaces.rest.resources.ParameterNameResource;

public class HvofSubsystemResourceFromEntityAssembler {
    public static HvofSubsystemResource toResourceFromEntity(HvofSubsystem entity, Long hvofSystemId) {
        return new HvofSubsystemResource(
                entity.getId(),
                hvofSystemId,
                entity.getSubsystemType().name(),
                entity.getName(),
                entity.getAlias(),
                entity.getParameters().stream().map(ParameterNameResource::new).toList()
        );
    }
}