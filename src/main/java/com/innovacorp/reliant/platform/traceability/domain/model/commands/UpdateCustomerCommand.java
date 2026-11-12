package com.innovacorp.reliant.platform.traceability.domain.model.commands;

public record UpdateCustomerCommand(Long customerId, Long linkedAssetOwnerOrganizationId, String legalName, String mineSite) {
}
