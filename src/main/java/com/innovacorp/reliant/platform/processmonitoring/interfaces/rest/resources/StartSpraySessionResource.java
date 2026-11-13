package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "StartSpraySessionRequest", example = "{\"hvofSystemId\": 1, \"recuperationId\": 1, \"recipeNumber\": 12, \"timeZone\": \"America/Lima\"}")
public record StartSpraySessionResource(
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Long hvofSystemId,
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Long recuperationId,
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Integer recipeNumber,
    String timeZone
) {
}
