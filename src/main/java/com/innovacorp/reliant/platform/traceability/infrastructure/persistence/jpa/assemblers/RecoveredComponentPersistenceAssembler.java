package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.RecoveredComponent;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities.RecoveredComponentPersistenceEntity;

public final class RecoveredComponentPersistenceAssembler {

    private RecoveredComponentPersistenceAssembler() {
    }

    public static RecoveredComponent toDomainFromPersistence(RecoveredComponentPersistenceEntity entity) {
        if (entity == null) {
            return null;
        }

        return RecoveredComponent.reconstitute(
                entity.getId(), entity.getCustomerId(), entity.getSerialNumber(), entity.getPartNumber(),
                entity.getComponentType(), entity.getMachineManufacturer(), entity.getMachineModel(),
                entity.getPcrTargetHours(), entity.getStatus());
    }

    public static RecoveredComponentPersistenceEntity toPersistenceFromDomain(RecoveredComponent component) {
        if (component == null) {
            return null;
        }

        var entity = new RecoveredComponentPersistenceEntity();
        if (component.getId() != null) {
            entity.setId(component.getId());
        }
        entity.setCustomerId(component.getCustomerId());
        entity.setSerialNumber(component.getSerialNumber());
        entity.setPartNumber(component.getPartNumber());
        entity.setComponentType(component.getComponentType());
        entity.setMachineManufacturer(component.getMachineManufacturer());
        entity.setMachineModel(component.getMachineModel());
        entity.setPcrTargetHours(component.getPcrTargetHours());
        entity.setStatus(component.getStatus());
        return entity;
    }
}