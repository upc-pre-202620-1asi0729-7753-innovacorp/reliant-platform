package com.innovacorp.reliant.platform.iam.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "SignInRequest",
        description = "Credentials to obtain a token",
        example = "{\"email\": \"cnavarro@fesa.com.pe\", \"password\": \"Reliant2026\"}"
)
public record SignInResource(

        @NotBlank(message = "{validation.not-blank}")
        @Email(message = "{validation.email}")
        @Schema(description = "Email", example = "cnavarro@fesa.com.pe")
        String email,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(description = "Password", example = "Reliant2026")
        String password

) {}