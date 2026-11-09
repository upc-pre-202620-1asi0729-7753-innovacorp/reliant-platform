package com.innovacorp.reliant.platform.traceability.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CustomerResponse")
public record CustomerResource(
        Long id,
        Long supplierOrganizationId,
        Long linkedAssetOwnerOrganizationId,
        String legalName,
        String ruc,
        String mineSite
) {
}