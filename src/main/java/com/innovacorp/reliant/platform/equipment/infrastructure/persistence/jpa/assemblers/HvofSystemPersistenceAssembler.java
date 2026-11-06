package com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.equipment.domain.model.aggregates.HvofSystem;
import com.innovacorp.reliant.platform.equipment.infrastructure.persistence.jpa.entities.HvofSystemPersistenceEntity;

/**
 * Static assembler between HVOF system domain and persistence representations.
 */
public final class HvofSystemPersistenceAssembler {

    private HvofSystemPersistenceAssembler() {
    }

    public static HvofSystem toDomainFromPersistence(HvofSystemPersistenceEntity entity) {
        if (entity == null) return null;
        return HvofSystem.reconstitute(
                entity.getId(),
                entity.getOrganizationId(),
                entity.getCode(),
                entity.getSerialNumber(),
                entity.getSystemManufacturer(),
                entity.getSystemModel(),
                entity.getFuelType(),
                entity.getStatus());
    }

    public static HvofSystemPersistenceEntity toPersistenceFromDomain(HvofSystem hvofSystem) {
        if (hvofSystem == null) return null;
        var entity = new HvofSystemPersistenceEntity();
        if (hvofSystem.getId() != null) {
            entity.setId(hvofSystem.getId());
        }
        entity.setOrganizationId(hvofSystem.getOrganizationId());
        entity.setCode(hvofSystem.getCode());
        entity.setSerialNumber(hvofSystem.getSerialNumber());
        entity.setSystemManufacturer(hvofSystem.getSystemManufacturer());
        entity.setSystemModel(hvofSystem.getSystemModel());
        entity.setFuelType(hvofSystem.getFuelType());
        entity.setStatus(hvofSystem.getStatus());
        return entity;
    }
}