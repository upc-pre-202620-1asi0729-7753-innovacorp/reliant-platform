package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "AbortSpraySessionRequest", example = "{\"reason\": \"Powder feeder clogged\"}")
public record AbortSpraySessionResource(@NotBlank(message = "{validation.not-blank}") String reason) {
}
