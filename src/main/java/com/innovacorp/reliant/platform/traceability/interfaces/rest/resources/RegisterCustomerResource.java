package com.innovacorp.reliant.platform.traceability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(name = "RegisterCustomerRequest")
public record RegisterCustomerResource(
        @Positive(message = "{validation.positive}")
        @Schema(description = "Asset owner organization id, if the customer uses Reliant", example = "2")
        Long linkedAssetOwnerOrganizationId,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "Sociedad Minera Cerro Verde S.A.A.")
        String legalName,

        @NotBlank(message = "{validation.not-blank}")
        @Pattern(regexp = "^\\d{11}$", message = "{validation.pattern}")
        @Schema(example = "20170072465")
        String ruc,

        @NotBlank(message = "{validation.not-blank}")
        @Schema(example = "Cerro Verde")
        String mineSite
) {
}