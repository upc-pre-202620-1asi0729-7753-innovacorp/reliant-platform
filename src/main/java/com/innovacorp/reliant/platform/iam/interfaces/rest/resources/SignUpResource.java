package com.innovacorp.reliant.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Resource for signing up: registers an organization and its first administrator.
 */
@Schema(
    name = "SignUpRequest",
    description = "Request payload for registering an organization and its administrator",
    example = "{\"organizationName\": \"Ferreyros S.A.\", \"ruc\": \"20100027292\", \"organizationType\": \"RECUPERATION_SUPPLIER\", \"fullName\": \"Carolina Navarro\", \"email\": \"cnavarro@fesa.com.pe\", \"password\": \"Reliant2026\"}"
)
public record SignUpResource(
    @NotBlank(message = "{validation.not-blank}")
    @Size(max = 150, message = "{validation.size}")
    @Schema(description = "Organization legal name", example = "Ferreyros S.A.")
    String organizationName,

    @NotBlank(message = "{validation.not-blank}")
    @Pattern(regexp = "^\\d{11}$", message = "{validation.pattern}")
    @Schema(description = "Peruvian tax id, 11 digits", example = "20100027292")
    String ruc,

    @NotBlank(message = "{validation.not-blank}")
    @Schema(description = "Organization type", example = "RECUPERATION_SUPPLIER", allowableValues = {"RECUPERATION_SUPPLIER", "ASSET_OWNER"})
    String organizationType,

    @NotBlank(message = "{validation.not-blank}")
    @Size(max = 120, message = "{validation.size}")
    @Schema(description = "Administrator full name", example = "Carolina Navarro")
    String fullName,

    @NotBlank(message = "{validation.not-blank}")
    @Email(message = "{validation.email}")
    @Schema(description = "Administrator email (used as username)", example = "cnavarro@fesa.com.pe")
    String email,

    @NotBlank(message = "{validation.not-blank}")
    @Size(min = 8, max = 72, message = "{validation.size}")
    @Schema(description = "Password", example = "Reliant2026", minLength = 8, maxLength = 72)
    String password
) {
}
