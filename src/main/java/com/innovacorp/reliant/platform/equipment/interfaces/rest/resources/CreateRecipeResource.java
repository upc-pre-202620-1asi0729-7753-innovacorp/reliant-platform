package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(
        name = "CreateRecipeRequest",
        description = "Request payload for creating a draft recipe"
)
public record CreateRecipeResource(
        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        @Schema(description = "HVOF system id", example = "1")
        Long hvofSystemId,

        @NotNull(message = "{validation.not-blank}")
        @Positive(message = "{validation.positive}")
        @Schema(description = "Recipe number on the PLC", example = "12")
        Integer recipeNumber,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Recipe name", example = "WC-Co 88/12 hydraulic rods")
        String name,

        @Schema(description = "Powder specification", example = "WOKA 3102")
        String powderSpecification,

        List<@Valid RecipeApplicabilityResource> applicabilities,

        @NotEmpty(message = "{validation.not-empty}")
        List<@Valid RecipeParameterResource> parameters
) {
}
