package com.innovacorp.reliant.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RoleResponse", description = "Role information response", example = "{\"id\": 1, \"name\": \"ROLE_ORG_ADMIN\"}")
public record RoleResource(
    @Schema(description = "Role unique identifier", example = "1") Long id,
    @Schema(description = "Role name", example = "ROLE_ORG_ADMIN") String name
) {
}
