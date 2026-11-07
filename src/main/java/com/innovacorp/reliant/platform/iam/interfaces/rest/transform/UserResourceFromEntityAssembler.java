package com.innovacorp.reliant.platform.iam.interfaces.rest.transform;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.interfaces.rest.resources.UserResource;

import java.util.Comparator;

public class UserResourceFromEntityAssembler {

    public static UserResource toResourceFromEntity(User user) {
        var roles = user.getRoles().stream()
                .sorted(Comparator.comparing(role -> role.getId()))
                .map(RoleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return new UserResource(
                user.getId(),
                user.getEmailValue(),
                user.getFullName(),
                user.getOrganizationIdValue(),
                roles);
    }
}