package com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.traceability.domain.model.aggregates.Recuperation;
import com.innovacorp.reliant.platform.traceability.infrastructure.persistence.jpa.entities.RecuperationPersistenceEntity;

import java.util.ArrayList;

public final class RecuperationPersistenceAssembler {

    private RecuperationPersistenceAssembler() {
    }

    public static Recuperation toDomainFromPersistence(RecuperationPersistenceEntity e) {
        if (e == null) return null;
        return Recuperation.reconstitute(e.getId(), e.getSupplierOrganizationId(), e.getWorkOrderNumber(), e.getManufacturingOrderNumber(), e.getComponentId(),
                e.getCustomerId(), e.getSegment(), e.getOperation(), e.getWeightValue(), e.getWeightUnitSymbol(), e.getHourmeterAtEntry(), e.getPowderSupplier(),
                e.getPowderLotNumber(), e.getPowderChemicalComposition(), e.getStatus(), e.getReceivedAt(), e.getCompletedAt(), new ArrayList<>(e.getLinkedSessionIds()));
    }

    public static RecuperationPersistenceEntity toPersistenceFromDomain(Recuperation r) {
        if (r == null) return null;
        var e = new RecuperationPersistenceEntity();
        if (r.getId() != null) {
            e.setId(r.getId());
        }
        e.setSupplierOrganizationId(r.getSupplierOrganizationId());
        e.setWorkOrderNumber(r.getWorkOrderNumber());
        e.setManufacturingOrderNumber(r.getManufacturingOrderNumber());
        e.setComponentId(r.getComponentId());
        e.setCustomerId(r.getCustomerId());
        e.setSegment(r.getSegment());
        e.setOperation(r.getOperation());
        e.setWeightValue(r.getWeightValue());
        e.setWeightUnitSymbol(r.getWeightUnitSymbol());
        e.setHourmeterAtEntry(r.getHourmeterAtEntry());
        e.setPowderSupplier(r.getPowderSupplier());
        e.setPowderLotNumber(r.getPowderLotNumber());
        e.setPowderChemicalComposition(r.getPowderChemicalComposition());
        e.setStatus(r.getStatus());
        e.setReceivedAt(r.getReceivedAt());
        e.setCompletedAt(r.getCompletedAt());
        e.setLinkedSessionIds(new ArrayList<>(r.getLinkedSessionIds()));
        return e;
    }
}
