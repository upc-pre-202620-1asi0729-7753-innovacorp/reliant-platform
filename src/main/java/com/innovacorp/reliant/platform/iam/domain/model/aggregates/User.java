package com.innovacorp.reliant.platform.iam.domain.model.aggregates;

import com.innovacorp.reliant.platform.iam.domain.model.entities.Role;
import com.innovacorp.reliant.platform.iam.domain.model.events.UserSignedUpEvent;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.UserStatus;
import com.innovacorp.reliant.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * User aggregate root. The email is the username; the password is stored hashed.
 */
@Getter
public class User extends AbstractDomainAggregateRoot<User> {
    private Long id;
    private final EmailAddress emailAddress;
    private final String fullName;
    private final String password;
    private final OrganizationId organizationId;
    private UserStatus status;
    private Set<Role> roles;

    public User(EmailAddress emailAddress, String fullName, String password, OrganizationId organizationId) {
        if (fullName == null || fullName.isBlank()) throw new IllegalArgumentException("Full name must not be blank");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password hash must not be blank");
        this.emailAddress = Objects.requireNonNull(emailAddress, "emailAddress must not be null");
        this.fullName = fullName.trim();
        this.password = password;
        this.organizationId = Objects.requireNonNull(organizationId, "organizationId must not be null");
        this.status = UserStatus.ACTIVE;
        this.roles = new HashSet<>();
    }

    public User(EmailAddress emailAddress, String fullName, String password, OrganizationId organizationId, List<Role> roles) {
        this(emailAddress, fullName, password, organizationId);
        addRoles(roles);
    }

    public static User reconstitute(Long id, EmailAddress emailAddress, String fullName, String password,
                                    OrganizationId organizationId, UserStatus status, Set<Role> roles) {
        var user = new User(emailAddress, fullName, password, organizationId);
        user.id = id;
        user.status = status;
        user.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        return user;
    }

    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    public User addRoles(List<Role> roles) {
        this.roles.addAll(Role.validateRoleSet(roles));
        return this;
    }

    public User replaceRoles(List<Role> roles) {
        this.roles = new HashSet<>(Role.validateRoleSet(roles));
        return this;
    }

    public String getEmailValue() {
        return emailAddress.address();
    }

    public Long getOrganizationIdValue() {
        return organizationId.organizationId();
    }

    public void onCreated() {
        registerDomainEvent(UserSignedUpEvent.from(this));
    }
}
