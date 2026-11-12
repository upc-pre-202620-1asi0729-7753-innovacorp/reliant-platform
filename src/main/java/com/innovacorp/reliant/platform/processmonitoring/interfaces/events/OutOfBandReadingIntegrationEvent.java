package com.innovacorp.reliant.platform.processmonitoring.interfaces.events;

/**
 * A process reading was classified as WARNING or SHUTDOWN.
 */
public record OutOfBandReadingIntegrationEvent(
        Long spraySessionId,
        Long hvofSystemId,
        Long organizationId,
        String parameter,
        String band,
        double value,
        long epochMillis,
        String unitSymbol
) {
}