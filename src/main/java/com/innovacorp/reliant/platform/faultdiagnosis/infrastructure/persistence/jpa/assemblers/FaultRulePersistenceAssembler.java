package com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.faultdiagnosis.domain.model.aggregates.FaultRule;
import com.innovacorp.reliant.platform.faultdiagnosis.infrastructure.persistence.jpa.entities.FaultRulePersistenceEntity;

public final class FaultRulePersistenceAssembler {

    private FaultRulePersistenceAssembler() {
    }

    public static FaultRule toDomainFromPersistence(FaultRulePersistenceEntity e) {
        if (e == null) return null;
        return FaultRule.reconstitute(e.getId(), e.getOrganizationId(), e.getParameter(), e.getBand(), e.getProbableCause(), e.getRecommendedAction(), e.getCooldownSeconds());
    }

    public static FaultRulePersistenceEntity toPersistenceFromDomain(FaultRule r) {
        if (r == null) return null;
        var e = new FaultRulePersistenceEntity();
        if (r.getId() != null) {
            e.setId(r.getId());
        }
        e.setOrganizationId(r.getOrganizationId());
        e.setParameter(r.getParameter());
        e.setBand(r.getBand());
        e.setProbableCause(r.getProbableCause());
        e.setRecommendedAction(r.getRecommendedAction());
        e.setCooldownSeconds(r.getCooldownSeconds());
        return e;
    }
}
