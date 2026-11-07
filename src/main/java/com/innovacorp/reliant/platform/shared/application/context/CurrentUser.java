package com.innovacorp.reliant.platform.shared.application.context;

import java.util.Set;

/**
 * Snapshot of the authenticated user, exposed to every bounded context without importing iam.domain.
 */
public record CurrentUser(
        Long userId,
        Long organizationId,
        String organizationType,
        String email,
        Set<String> roles
) {
    public boolean hasRole(String role) {
        return roles.contains(role);
    }

    public boolean isSupplier() {
        return "RECUPERATION_SUPPLIER".equals(organizationType);
    }

    public boolean isAssetOwner() {
        return "ASSET_OWNER".equals(organizationType);
    }
}