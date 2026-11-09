package com.innovacorp.reliant.platform.traceability.domain.model.commands;

public record RegisterCustomerCommand(Long supplierOrganizationId, Long linkedAssetOwnerOrganizationId,
                                      String legalName, String ruc, String mineSite) {
}