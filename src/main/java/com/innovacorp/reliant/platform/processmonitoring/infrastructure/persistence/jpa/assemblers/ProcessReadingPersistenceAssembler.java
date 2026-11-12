package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities.ProcessReadingPersistenceEntity;

public final class ProcessReadingPersistenceAssembler {

    private ProcessReadingPersistenceAssembler() {
    }

    public static ProcessReading toDomainFromPersistence(ProcessReadingPersistenceEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProcessReading.reconstitute(
                entity.getId(), entity.getSpraySessionId(), entity.getEpochMillis(),
                entity.getPlcClockOffsetMillis(), entity.getTagPath(), entity.getParameter(),
                entity.getSubsystemId(), entity.getPartId(), entity.getValue(),
                entity.getUnitSymbol(), entity.getUnitCategory(), entity.getBand(),
                entity.isDerived(), entity.isMappingPending());
    }

    public static ProcessReadingPersistenceEntity toPersistenceFromDomain(ProcessReading reading) {
        if (reading == null) {
            return null;
        }

        var entity = new ProcessReadingPersistenceEntity();
        if (reading.getId() != null) {
            entity.setId(reading.getId());
        }
        entity.setSpraySessionId(reading.getSpraySessionId());
        entity.setEpochMillis(reading.getEpochMillis());
        entity.setPlcClockOffsetMillis(reading.getPlcClockOffsetMillis());
        entity.setTagPath(reading.getTagPath());
        entity.setParameter(reading.getParameter());
        entity.setSubsystemId(reading.getSubsystemId());
        entity.setPartId(reading.getPartId());
        entity.setValue(reading.getValue());
        entity.setUnitSymbol(reading.getUnitSymbol());
        entity.setUnitCategory(reading.getUnitCategory());
        entity.setBand(reading.getBand());
        entity.setDerived(reading.isDerived());
        entity.setMappingPending(reading.isMappingPending());
        return entity;
    }
}