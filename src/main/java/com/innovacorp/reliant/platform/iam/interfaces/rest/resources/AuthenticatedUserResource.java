package com.innovacorp.reliant.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
    name = "AuthenticatedUserResponse",
    description = "Authenticated user information with JWT token",
    example = "{\"id\": 1, \"email\": \"cnavarro@fesa.com.pe\", \"fullName\": \"Carolina Navarro\", \"organizationId\": 1, \"organizationType\": \"RECUPERATION_SUPPLIER\", \"roleIds\": [{\"roleId\": 1}], \"token\": \"eyJhbGciOiJIUzI1NiIs...\"}"
)
public record AuthenticatedUserResource(
    @Schema(description = "User id", example = "1") Long id,
    @Schema(description = "Email", example = "cnavarro@fesa.com.pe") String email,
    @Schema(description = "Full name", example = "Carolina Navarro") String fullName,
    @Schema(description = "Organization id", example = "1") Long organizationId,
    @Schema(description = "Organization type", example = "RECUPERATION_SUPPLIER") String organizationType,
    @Schema(description = "Assigned role ids") List<RoleIdResource> roleIds,
    @Schema(description = "JWT Bearer token") String token
) {
}
