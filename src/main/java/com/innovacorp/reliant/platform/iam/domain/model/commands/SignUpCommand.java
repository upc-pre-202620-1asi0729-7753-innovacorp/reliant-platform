package com.innovacorp.reliant.platform.iam.domain.model.commands;

/**
 * Sign Up Command: registers an organization and its first administrator.
 */
public record SignUpCommand(
        String organizationName,
        String ruc,
        String organizationType,
        String fullName,
        String email,
        String password
) {
}
