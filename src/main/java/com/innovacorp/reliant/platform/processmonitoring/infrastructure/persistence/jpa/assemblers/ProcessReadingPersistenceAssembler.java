package com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.infrastructure.persistence.jpa.entities.ProcessReadingPersistenceEntity;

public final class ProcessReadingPersistenceAssembler {

    private ProcessReadingPersistenceAssembler() {
    }

    public static ProcessReading toDomainFromPersistence(ProcessReadingPersistenceEntity e) {
        if (e == null) return null;
        return ProcessReading.reconstitute(e.getId(), e.getSpraySessionId(), e.getEpochMillis(), e.getPlcClockOffsetMillis(), e.getTagPath(), e.getParameter(),
                e.getSubsystemId(), e.getPartId(), e.getValue(), e.getUnitSymbol(), e.getUnitCategory(), e.getBand(), e.isDerived(), e.isMappingPending());
    }

    public static ProcessReadingPersistenceEntity toPersistenceFromDomain(ProcessReading r) {
        if (r == null) return null;
        var e = new ProcessReadingPersistenceEntity();
        if (r.getId() != null) {
            e.setId(r.getId());
        }
        e.setSpraySessionId(r.getSpraySessionId());
        e.setEpochMillis(r.getEpochMillis());
        e.setPlcClockOffsetMillis(r.getPlcClockOffsetMillis());
        e.setTagPath(r.getTagPath());
        e.setParameter(r.getParameter());
        e.setSubsystemId(r.getSubsystemId());
        e.setPartId(r.getPartId());
        e.setValue(r.getValue());
        e.setUnitSymbol(r.getUnitSymbol());
        e.setUnitCategory(r.getUnitCategory());
        e.setBand(r.getBand());
        e.setDerived(r.isDerived());
        e.setMappingPending(r.isMappingPending());
        return e;
    }
}
