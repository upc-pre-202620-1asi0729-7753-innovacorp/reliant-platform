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

    public static SpraySession toDomainFromPersistence(SpraySessionPersistenceEntity entity) {
        if (entity == null) {
            return null;
        }

        var recipeBands = entity.getRecipeBands().stream()
                .map(band -> new RecipeBand(
                        band.getParameter(), band.getSetpoint(), band.getLowerShutdown(),
                        band.getLowerWarning(), band.getNominalMin(), band.getNominalMax(),
                        band.getUpperWarning(), band.getUpperShutdown(),
                        band.getUnitSymbol(), band.getUnitCategory()))
                .toList();

        var passes = entity.getPasses().stream()
                .map(pass -> SprayPass.reconstitute(
                        pass.getId(), pass.getPassNumber(), pass.getStartedAt(), pass.getEndedAt()))
                .toList();

        return SpraySession.reconstitute(
                entity.getId(), entity.getOrganizationId(), entity.getHvofSystemId(),
                entity.getRecuperationId(), entity.getOperatorId(), entity.getRecipeNumber(),
                entity.getStartedAt(), entity.getEndedAt(), entity.getTimeZone(), entity.getStatus(),
                entity.getAbortReason(), recipeBands, passes);
    }

    public static SpraySessionPersistenceEntity toPersistenceFromDomain(SpraySession session) {
        if (session == null) {
            return null;
        }

        var entity = new SpraySessionPersistenceEntity();
        if (session.getId() != null) {
            entity.setId(session.getId());
        }
        entity.setOrganizationId(session.getOrganizationId());
        entity.setHvofSystemId(session.getHvofSystemId());
        entity.setRecuperationId(session.getRecuperationId());
        entity.setOperatorId(session.getOperatorId());
        entity.setRecipeNumber(session.getRecipeNumber());
        entity.setStartedAt(session.getStartedAt());
        entity.setEndedAt(session.getEndedAt());
        entity.setTimeZone(session.getTimeZone());
        entity.setStatus(session.getStatus());
        entity.setAbortReason(session.getAbortReason());
        entity.setRecipeBands(session.getRecipeBands().stream()
                .map(band -> new RecipeBandPersistenceEmbeddable(
                        band.parameter(), band.setpoint(), band.lowerShutdown(), band.lowerWarning(),
                        band.nominalMin(), band.nominalMax(), band.upperWarning(), band.upperShutdown(),
                        band.unitSymbol(), band.unitCategory()))
                .collect(Collectors.toCollection(ArrayList::new)));
        entity.setPasses(session.getPasses().stream()
                .map(pass -> toPersistenceFromDomain(pass, entity))
                .collect(Collectors.toCollection(ArrayList::new)));
        return entity;
    }

    private static SprayPassPersistenceEntity toPersistenceFromDomain(
            SprayPass pass, SpraySessionPersistenceEntity session) {
        var entity = new SprayPassPersistenceEntity();
        if (pass.getId() != null) {
            entity.setId(pass.getId());
        }
        entity.setSpraySession(session);
        entity.setPassNumber(pass.getPassNumber());
        entity.setStartedAt(pass.getStartedAt());
        entity.setEndedAt(pass.getEndedAt());
        return entity;
    }
}