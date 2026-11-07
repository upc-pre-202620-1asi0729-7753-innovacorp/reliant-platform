package com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.entities;

import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.EmailAddress;
import com.innovacorp.reliant.platform.iam.domain.model.valueobjects.UserStatus;
import com.innovacorp.reliant.platform.iam.infrastructure.persistence.jpa.converters.EmailAddressPersistenceConverter;
import com.innovacorp.reliant.platform.shared.domain.model.valueobjects.OrganizationId;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.converters.OrganizationIdPersistenceConverter;
import com.innovacorp.reliant.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Convert(converter = EmailAddressPersistenceConverter.class)
    @Column(name = "email_address", nullable = false, unique = true, length = 120)
    private EmailAddress emailAddress;

    @Column(name = "full_name", nullable = false, length = 120)
    private String fullName;

    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @Convert(converter = OrganizationIdPersistenceConverter.class)
    @Column(name = "organization_id", nullable = false)
    private OrganizationId organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RolePersistenceEntity> roles = new HashSet<>();
}