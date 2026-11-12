package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "StartSpraySessionRequest")
public record StartSpraySessionResource(
        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        Long hvofSystemId,

        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        Long recuperationId,

        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        Integer recipeNumber,

        @Schema(example = "America/Lima")
        String timeZone
) {
}