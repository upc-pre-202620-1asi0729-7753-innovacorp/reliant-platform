package com.innovacorp.reliant.platform.iam.interfaces.rest.transform;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.Organization;
import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.RoleIdResource;

import java.util.Comparator;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, Organization organization, String token) {
        var roleIds = user.getRoles().stream()
                .sorted(Comparator.comparing(role -> role.getId()))
                .map(role -> new RoleIdResource(role.getId()))
                .toList();
        return new AuthenticatedUserResource(
                user.getId(),
                user.getEmailValue(),
                user.getFullName(),
                user.getOrganizationIdValue(),
                organization.getOrganizationType().name(),
                roleIds,
                token);
    }
}
