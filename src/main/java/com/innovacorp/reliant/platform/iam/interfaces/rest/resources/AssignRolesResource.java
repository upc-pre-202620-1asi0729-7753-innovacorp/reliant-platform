package com.innovacorp.reliant.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Schema(
        name = "AssignRolesRequest",
        description = "Replaces the roles of a user",
        example = "{\"roleIds\": [1, 4]}"
)
public record AssignRolesResource(
        @NotEmpty(message = "{validation.not-empty}")
        @Schema(description = "Role ids to assign", example = "[1, 4]")
        List<@Positive(message = "{validation.positive}") Long> roleIds
) {
}