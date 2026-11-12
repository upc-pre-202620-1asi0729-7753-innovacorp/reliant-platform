package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.aggregates.SpraySession;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.SprayPass;
import com.innovacorp.reliant.platform.processmonitoring.domain.model.valueobjects.RecipeBand;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.embeddables.RecipeBandPersistenceEmbeddable;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities.SprayPassPersistenceEntity;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities.SpraySessionPersistenceEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public final class SpraySessionPersistenceAssembler {

    private SpraySessionPersistenceAssembler() {
    }

    public static SpraySession toDomainFromPersistence(SpraySessionPersistenceEntity e) {
        if (e == null) return null;
        return SpraySession.reconstitute(e.getId(), e.getOrganizationId(), e.getHvofSystemId(), e.getRecuperationId(), e.getOperatorId(), e.getRecipeNumber(),
                e.getStartedAt(), e.getEndedAt(), e.getTimeZone(), e.getStatus(), e.getAbortReason(),
                e.getRecipeBands().stream().map(b -> new RecipeBand(b.getParameter(), b.getSetpoint(), b.getLowerShutdown(), b.getLowerWarning(), b.getNominalMin(),
                        b.getNominalMax(), b.getUpperWarning(), b.getUpperShutdown(), b.getUnitSymbol(), b.getUnitCategory())).toList(),
                e.getPasses().stream().map(p -> SprayPass.reconstitute(p.getId(), p.getPassNumber(), p.getStartedAt(), p.getEndedAt())).toList());
    }

    public static SpraySessionPersistenceEntity toPersistenceFromDomain(SpraySession s) {
        if (s == null) return null;
        var e = new SpraySessionPersistenceEntity();
        if (s.getId() != null) {
            e.setId(s.getId());
        }
        e.setOrganizationId(s.getOrganizationId());
        e.setHvofSystemId(s.getHvofSystemId());
        e.setRecuperationId(s.getRecuperationId());
        e.setOperatorId(s.getOperatorId());
        e.setRecipeNumber(s.getRecipeNumber());
        e.setStartedAt(s.getStartedAt());
        e.setEndedAt(s.getEndedAt());
        e.setTimeZone(s.getTimeZone());
        e.setStatus(s.getStatus());
        e.setAbortReason(s.getAbortReason());
        e.setRecipeBands(s.getRecipeBands().stream()
                .map(b -> new RecipeBandPersistenceEmbeddable(b.parameter(), b.setpoint(), b.lowerShutdown(), b.lowerWarning(), b.nominalMin(), b.nominalMax(),
                        b.upperWarning(), b.upperShutdown(), b.unitSymbol(), b.unitCategory()))
                .collect(Collectors.toCollection(ArrayList::new)));
        e.setPasses(s.getPasses().stream().map(p -> toPersistenceFromDomain(p, e)).collect(Collectors.toCollection(ArrayList::new)));
        return e;
    }

    private static SprayPassPersistenceEntity toPersistenceFromDomain(SprayPass p, SpraySessionPersistenceEntity session) {
        var e = new SprayPassPersistenceEntity();
        if (p.getId() != null) {
            e.setId(p.getId());
        }
        e.setSpraySession(session);
        e.setPassNumber(p.getPassNumber());
        e.setStartedAt(p.getStartedAt());
        e.setEndedAt(p.getEndedAt());
        return e;
    }
}
