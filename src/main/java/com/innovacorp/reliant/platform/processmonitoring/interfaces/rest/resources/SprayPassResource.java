package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(name = "SprayPassResponse")
public record SprayPassResource(Long id, Long spraySessionId, int passNumber, Instant startedAt, Instant endedAt) {
}
