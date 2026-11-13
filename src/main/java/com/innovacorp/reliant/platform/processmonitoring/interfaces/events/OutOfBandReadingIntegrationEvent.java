package com.innovacorp.reliant.platform.processmonitoring.interfaces.events;

/**
 * Integration event: a reading fell in WARNING or SHUTDOWN. Published for other bounded contexts (Fault Diagnosis).
 */
public record OutOfBandReadingIntegrationEvent(Long spraySessionId, Long hvofSystemId, Long organizationId, String parameter, String band,
                                               double value, long epochMillis, String unitSymbol) {
}
