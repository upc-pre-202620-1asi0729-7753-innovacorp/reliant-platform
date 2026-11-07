package com.innovacorp.reliant.platform.iam.domain.model.events;

import com.innovacorp.reliant.platform.iam.domain.model.aggregates.User;

/**
 * Domain event published when a new user is persisted.
 */
public record UserSignedUpEvent(Long userId, String email, Long organizationId) {

    public static UserSignedUpEvent from(User user) {
        return new UserSignedUpEvent(
                user.getId(),
                user.getEmailValue(),
                user.getOrganizationIdValue()
        );
    }
}
