package com.innovacorp.reliant.platform.traceability.domain.model.commands;

/**
 * Command to link every customer registered with the given RUC to the asset-owner organization that owns that RUC.
 */
public record LinkAssetOwnerToCustomersCommand(String ruc, Long assetOwnerOrganizationId) {
}
