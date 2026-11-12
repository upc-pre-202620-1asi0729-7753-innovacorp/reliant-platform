package com.innovacorp.reliant.platform.processmonitoring.domain.model.events;

import com.innovacorp.reliant.platform.processmonitoring.domain.model.entities.ProcessReading;

/**
 * Raised by the aggregate when a reading falls in WARNING or SHUTDOWN. Republished as an integration event (Part 6 consumes it).
 */
public record OutOfBandReadingDetectedEvent(Long spraySessionId, Long hvofSystemId, Long organizationId, String parameter, String band,
                                            double value, long epochMillis, String unitSymbol) {
    public static OutOfBandReadingDetectedEvent from(ProcessReading r, Long hvofSystemId, Long organizationId) {
        return new OutOfBandReadingDetectedEvent(r.getSpraySessionId(), hvofSystemId, organizationId, r.getParameter(), r.getBand().name(),
                r.getValue(), r.getEpochMillis(), r.getUnitSymbol());
    }
}
