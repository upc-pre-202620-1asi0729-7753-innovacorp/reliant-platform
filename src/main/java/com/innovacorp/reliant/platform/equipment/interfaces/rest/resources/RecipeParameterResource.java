package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(
        name = "RecipeParameter",
        description = "Process parameter with setpoint and bands",
        example = "{\"parameter\": \"fuel_gas_flow\", \"setpoint\": 160, \"lowerShutdown\": 140, \"lowerWarning\": 150, \"nominalMin\": 155, \"nominalMax\": 165, \"upperWarning\": 170, \"upperShutdown\": 180, \"unitSymbol\": \"lpm\", \"unitCategory\": \"flow\"}"
)
public record RecipeParameterResource(
        @NotBlank(message = "{validation.not-blank}")
        @Pattern(regexp = "^[a-z][a-z0-9_]{2,39}$", message = "{validation.pattern}")
        @Schema(description = "snake_case parameter name", example = "fuel_gas_flow")
        String parameter,

        @Schema(example = "160")
        double setpoint,

        @Schema(example = "140")
        double lowerShutdown,

        @Schema(example = "150")
        double lowerWarning,

        @Schema(example = "155")
        double nominalMin,

        @Schema(example = "165")
        double nominalMax,

        @Schema(example = "170")
        double upperWarning,

        @Schema(example = "180")
        double upperShutdown,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "lpm")
        String unitSymbol,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(
                example = "flow",
                allowableValues = {
                        "flow", "mass_flow", "pressure",
                        "temperature", "speed", "dimensionless"
                }
        )
        String unitCategory
) {
}
