package com.innovacorp.reliant.platform.traceability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(name = "UpdateCustomerRequest")
public record UpdateCustomerResource(
        @Positive(message = "{validation.positive}") Long linkedAssetOwnerOrganizationId,
        @NotBlank(message = "{validation.not-blank}") String legalName,
        @NotBlank(message = "{validation.not-blank}") String mineSite
) {
}