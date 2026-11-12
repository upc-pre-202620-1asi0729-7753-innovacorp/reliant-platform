package com.innovacorp.reliant.platform.processmonitoring.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(name = "IngestProcessReadingRequest", example = "{\"epochMillis\": 1761580800000, \"plcClockOffsetMillis\": 0, \"tagPath\": \"FuelGas.Flow.Actual\", \"parameter\": \"fuel_gas_flow\", \"subsystemId\": 1, \"partId\": 1, \"value\": 160.2}")
public record IngestProcessReadingResource(
    @NotNull(message = "{validation.not-blank}") @Positive(message = "{validation.positive}") Long epochMillis,
    Long plcClockOffsetMillis,
    @NotBlank(message = "{validation.not-blank}") String tagPath,
    @NotBlank(message = "{validation.not-blank}") String parameter,
    Long subsystemId,
    Long partId,
    @NotNull(message = "{validation.not-blank}") Double value
) {
}
