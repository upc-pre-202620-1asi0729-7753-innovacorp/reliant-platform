package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "UpdateFaultRuleRequest")
public record UpdateFaultRuleResource(
    @NotBlank(message = "{validation.not-blank}") String probableCause,
    @NotBlank(message = "{validation.not-blank}") String recommendedAction,
    @PositiveOrZero(message = "{validation.positive}") Integer cooldownSeconds
) {
}
