package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultCase;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.entities.FaultCasePersistenceEntity;

public final class FaultCasePersistenceAssembler {

    private FaultCasePersistenceAssembler() {
    }

    public static FaultCase toDomainFromPersistence(FaultCasePersistenceEntity e) {
        if (e == null) return null;
        return FaultCase.reconstitute(e.getId(), e.getOrganizationId(), e.getSpraySessionId(), e.getHvofSystemId(), e.getParameter(), e.getBand(),
                e.getValue(), e.getEpochMillis(), e.getProbableCause(), e.getRecommendedAction(), e.getStatus(),
                e.getAcknowledgedBy(), e.getAcknowledgedAt(), e.getResolvedBy(), e.getResolvedAt(), e.getResolutionNotes());
    }

    public static FaultCasePersistenceEntity toPersistenceFromDomain(FaultCase c) {
        if (c == null) return null;
        var e = new FaultCasePersistenceEntity();
        if (c.getId() != null) {
            e.setId(c.getId());
        }
        e.setOrganizationId(c.getOrganizationId());
        e.setSpraySessionId(c.getSpraySessionId());
        e.setHvofSystemId(c.getHvofSystemId());
        e.setParameter(c.getParameter());
        e.setBand(c.getBand());
        e.setValue(c.getValue());
        e.setEpochMillis(c.getEpochMillis());
        e.setProbableCause(c.getProbableCause());
        e.setRecommendedAction(c.getRecommendedAction());
        e.setStatus(c.getStatus());
        e.setAcknowledgedBy(c.getAcknowledgedBy());
        e.setAcknowledgedAt(c.getAcknowledgedAt());
        e.setResolvedBy(c.getResolvedBy());
        e.setResolvedAt(c.getResolvedAt());
        e.setResolutionNotes(c.getResolutionNotes());
        return e;
    }
}
