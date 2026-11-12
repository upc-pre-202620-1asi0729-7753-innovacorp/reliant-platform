package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.ProcessReadingResource;

public class ProcessReadingResourceFromEntityAssembler {
    public static ProcessReadingResource toResourceFromEntity(ProcessReading r) {
        return new ProcessReadingResource(r.getId(), r.getSpraySessionId(), r.getEpochMillis(), r.getPlcClockOffsetMillis(), r.getTagPath(), r.getParameter(),
                r.getSubsystemId(), r.getPartId(), r.getValue(), r.getUnitSymbol(), r.getUnitCategory(), r.getBand().name().toLowerCase(), r.isDerived(), r.isMappingPending());
    }
}
