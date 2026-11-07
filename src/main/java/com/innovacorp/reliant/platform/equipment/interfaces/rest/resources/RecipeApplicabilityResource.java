package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "RecipeApplicability",
        example = "{\"componentType\": \"HYDRAULIC_ROD\", \"machineModel\": \"PC5500\", \"position\": \"Boom cylinder\"}"
)
public record RecipeApplicabilityResource(
        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "HYDRAULIC_ROD")
        String componentType,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "PC5500")
        String machineModel,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "Boom cylinder")
        String position
) {
}