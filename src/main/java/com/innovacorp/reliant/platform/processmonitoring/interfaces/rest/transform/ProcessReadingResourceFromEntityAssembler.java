package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.transform;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;
import com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources.ProcessReadingResource;

import java.util.Locale;

public class ProcessReadingResourceFromEntityAssembler {

    public static ProcessReadingResource toResourceFromEntity(ProcessReading reading) {
        return new ProcessReadingResource(
                reading.getId(), reading.getSpraySessionId(), reading.getEpochMillis(),
                reading.getPlcClockOffsetMillis(), reading.getTagPath(), reading.getParameter(),
                reading.getSubsystemId(), reading.getPartId(), reading.getValue(),
                reading.getUnitSymbol(), reading.getUnitCategory(),
                reading.getBand().name().toLowerCase(Locale.ROOT),
                reading.isDerived(), reading.isMappingPending());
    }
}