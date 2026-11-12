package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "SpraySessionResponse")
public record SpraySessionResource(
        Long id,
        Long hvofSystemId,
        Long recuperationId,
        Long operatorId,
        Integer recipeNumber,
        Instant startedAt,
        Instant endedAt,
        String timeZone,
        String status,
        String abortReason
) {
}