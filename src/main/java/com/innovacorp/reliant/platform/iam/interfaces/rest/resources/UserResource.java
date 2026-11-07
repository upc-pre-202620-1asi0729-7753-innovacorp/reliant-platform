package com.innovacorp.reliant.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        name = "UserResponse",
        description = "User information response",
        example = "{\"id\": 1, \"email\": \"cnavarro@fesa.com.pe\", \"fullName\": \"Carolina Navarro\", \"organizationId\": 1, \"roles\": [{\"id\": 1, \"name\": \"ROLE_ORG_ADMIN\"}]}"
)
public record UserResource(
        @Schema(description = "User unique identifier", example = "1") Long id,
        @Schema(description = "Email (username)", example = "cnavarro@fesa.com.pe") String email,
        @Schema(description = "Full name", example = "Carolina Navarro") String fullName,
        @Schema(description = "Organization id", example = "1") Long organizationId,
        @Schema(description = "Assigned roles") List<RoleResource> roles
) {
}