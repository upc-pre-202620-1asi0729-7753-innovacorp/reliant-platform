package com.innovacorp.reliant.platform.equipment.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "RecipeResponse",
        description = "Recipe information response"
)
public record RecipeResource(
        @Schema(example = "1")
        Long id,

        @Schema(example = "1")
        Long hvofSystemId,

        @Schema(example = "12")
        Integer recipeNumber,

        @Schema(example = "WC-Co 88/12 hydraulic rods")
        String name,

        @Schema(example = "WOKA 3102")
        String powderSpecification,

        @Schema(example = "DRAFT")
        String status,

        List<RecipeApplicabilityResource> applicabilities,

        List<RecipeParameterResource> parameters
) {
}