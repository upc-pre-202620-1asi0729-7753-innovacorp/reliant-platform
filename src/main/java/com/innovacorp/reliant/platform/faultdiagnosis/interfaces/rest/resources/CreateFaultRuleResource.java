package com.innovacorp.reliant.platform.faultdiagnosis.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(name = "CreateFaultRuleRequest", example = "{\"parameter\": \"fuel_gas_flow\", \"band\": \"shutdown\", \"probableCause\": \"Fuel line pressure drop or regulator failure\", \"recommendedAction\": \"Stop spraying; inspect fuel regulator and supply pressure\", \"cooldownSeconds\": 60}")
public record CreateFaultRuleResource(
    @NotBlank(message = "{validation.not-blank}") @Pattern(regexp = "^[a-z][a-z0-9_]{2,39}$", message = "{validation.pattern}") String parameter,
    @NotBlank(message = "{validation.not-blank}") @Schema(allowableValues = {"warning", "shutdown"}) String band,
    @NotBlank(message = "{validation.not-blank}") String probableCause,
    @NotBlank(message = "{validation.not-blank}") String recommendedAction,
    @PositiveOrZero(message = "{validation.positive}") Integer cooldownSeconds
) {
}
