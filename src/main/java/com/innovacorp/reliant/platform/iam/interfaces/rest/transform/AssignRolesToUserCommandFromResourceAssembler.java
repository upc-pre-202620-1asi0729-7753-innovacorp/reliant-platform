package com.innovacorp.reliant.platform.iam.interfaces.rest.transform;

import com.innovacorp.reliant.platform.iam.domain.model.commands.AssignRolesToUserCommand;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.AssignRolesResource;

public class AssignRolesToUserCommandFromResourceAssembler {

    public static AssignRolesToUserCommand toCommandFromResource(
            Long userId,
            AssignRolesResource resource
    ) {
        return new AssignRolesToUserCommand(userId, resource.roleIds());
    }
}