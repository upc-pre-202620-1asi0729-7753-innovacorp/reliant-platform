package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.Instant;

@Schema(name = "RegisterSprayPassRequest", example = "{\"passNumber\": 1, \"startedAt\": \"2026-10-27T13:00:00Z\", \"endedAt\": \"2026-10-27T13:03:30Z\"}")
public record RegisterSprayPassResource(
    @Positive(message = "{validation.positive}") int passNumber,
    @NotNull(message = "{validation.not-blank}") Instant startedAt,
    Instant endedAt
) {
}
