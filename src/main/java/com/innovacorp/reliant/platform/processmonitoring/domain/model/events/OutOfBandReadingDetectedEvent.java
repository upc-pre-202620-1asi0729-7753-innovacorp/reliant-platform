package com.innovacorp.reliant.platform.processmonitoring.domain.model.events;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;

public record OutOfBandReadingDetectedEvent(
        Long spraySessionId,
        Long hvofSystemId,
        Long organizationId,
        String parameter,
        String band,
        double value,
        long epochMillis,
        String unitSymbol
) {
    public static OutOfBandReadingDetectedEvent from(ProcessReading reading, Long hvofSystemId,
                                                     Long organizationId) {
        return new OutOfBandReadingDetectedEvent(
                reading.getSpraySessionId(), hvofSystemId, organizationId, reading.getParameter(),
                reading.getBand().name(), reading.getValue(), reading.getEpochMillis(), reading.getUnitSymbol());
    }
}