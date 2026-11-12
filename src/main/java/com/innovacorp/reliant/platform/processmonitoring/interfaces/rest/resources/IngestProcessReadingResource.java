package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "IngestProcessReadingRequest")
public record IngestProcessReadingResource(
        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        @Schema(example = "1761580800000")
        Long epochMillis,

        Long plcClockOffsetMillis,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "FuelGas.Flow.Actual")
        String tagPath,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "fuel_gas_flow")
        String parameter,

        Long subsystemId,
        Long partId,

        @NotNull(message = "{validation.not-blank}")
        @Schema(example = "160.2")
        Double value
) {
}