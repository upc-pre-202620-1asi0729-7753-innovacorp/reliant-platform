package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

import java.util.stream.Collectors;

public final class UserPersistenceAssembler {

    private UserPersistenceAssembler() {
    }

    public static User toDomainFromPersistence(UserPersistenceEntity entity) {
        if (entity == null) return null;
        var roles = entity.getRoles().stream()
                .map(RolePersistenceAssembler::toDomainFromPersistence)
                .collect(Collectors.toSet());
        return User.reconstitute(entity.getId(), entity.getEmailAddress(), entity.getFullName(), entity.getPassword(),
                entity.getOrganizationId(), entity.getStatus(), roles);
    }

    public static UserPersistenceEntity toPersistenceFromDomain(User user) {
        if (user == null) return null;
        var entity = new UserPersistenceEntity();
        if (user.getId() != null) {
            entity.setId(user.getId());
        }
        entity.setEmailAddress(user.getEmailAddress());
        entity.setFullName(user.getFullName());
        entity.setPassword(user.getPassword());
        entity.setOrganizationId(user.getOrganizationId());
        entity.setStatus(user.getStatus());
        entity.setRoles(user.getRoles().stream()
                .map(RolePersistenceAssembler::toPersistenceFromDomain)
                .collect(Collectors.toSet()));
        return entity;
    }
}
