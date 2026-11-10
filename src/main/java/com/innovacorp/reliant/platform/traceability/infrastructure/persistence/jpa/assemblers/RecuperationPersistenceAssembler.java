package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities.RecuperationPersistenceEntity;

import java.util.ArrayList;

public final class RecuperationPersistenceAssembler {

    private RecuperationPersistenceAssembler() {
    }

    public static Recuperation toDomainFromPersistence(RecuperationPersistenceEntity entity) {
        if (entity == null) {
            return null;
        }

        return Recuperation.reconstitute(
                entity.getId(), entity.getSupplierOrganizationId(), entity.getWorkOrderNumber(),
                entity.getManufacturingOrderNumber(), entity.getComponentId(), entity.getCustomerId(),
                entity.getSegment(), entity.getOperation(), entity.getWeightValue(), entity.getWeightUnitSymbol(),
                entity.getHourmeterAtEntry(), entity.getPowderSupplier(), entity.getPowderLotNumber(),
                entity.getPowderChemicalComposition(), entity.getStatus(), entity.getReceivedAt(),
                entity.getCompletedAt(), new ArrayList<>(entity.getLinkedSessionIds()));
    }

    public static RecuperationPersistenceEntity toPersistenceFromDomain(Recuperation recuperation) {
        if (recuperation == null) {
            return null;
        }

        var entity = new RecuperationPersistenceEntity();
        if (recuperation.getId() != null) {
            entity.setId(recuperation.getId());
        }
        entity.setSupplierOrganizationId(recuperation.getSupplierOrganizationId());
        entity.setWorkOrderNumber(recuperation.getWorkOrderNumber());
        entity.setManufacturingOrderNumber(recuperation.getManufacturingOrderNumber());
        entity.setComponentId(recuperation.getComponentId());
        entity.setCustomerId(recuperation.getCustomerId());
        entity.setSegment(recuperation.getSegment());
        entity.setOperation(recuperation.getOperation());
        entity.setWeightValue(recuperation.getWeightValue());
        entity.setWeightUnitSymbol(recuperation.getWeightUnitSymbol());
        entity.setHourmeterAtEntry(recuperation.getHourmeterAtEntry());
        entity.setPowderSupplier(recuperation.getPowderSupplier());
        entity.setPowderLotNumber(recuperation.getPowderLotNumber());
        entity.setPowderChemicalComposition(recuperation.getPowderChemicalComposition());
        entity.setStatus(recuperation.getStatus());
        entity.setReceivedAt(recuperation.getReceivedAt());
        entity.setCompletedAt(recuperation.getCompletedAt());
        entity.setLinkedSessionIds(new ArrayList<>(recuperation.getLinkedSessionIds()));
        return entity;
    }
}