package com.innovacorp.reliant.platform.iam.application.commandservices;

import com.innovacorp.reliant.platform.iam.domain.model.commands.SeedRolesCommand;

/**
 * Application service contract for IAM role commands.
 */
public interface RoleCommandService {
    void handle(SeedRolesCommand command);
}
