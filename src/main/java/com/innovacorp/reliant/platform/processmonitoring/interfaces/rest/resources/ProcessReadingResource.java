package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProcessReadingResponse")
public record ProcessReadingResource(
        Long id,
        Long spraySessionId,
        long epochMillis,
        long plcClockOffsetMillis,
        String tagPath,
        String parameter,
        Long subsystemId,
        Long partId,
        double value,
        String unitSymbol,
        String unitCategory,
        String band,
        boolean derived,
        boolean mappingPending
) {
}